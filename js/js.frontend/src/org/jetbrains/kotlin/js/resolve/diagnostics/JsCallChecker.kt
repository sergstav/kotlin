/*
 * Copyright 2010-2015 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.resolve.diagnostics

import com.google.gwt.dev.js.AbortParsingException
import com.google.gwt.dev.js.rhino.*
import com.google.gwt.dev.js.rhino.Utils.*
import org.jetbrains.annotations.TestOnly
import org.jetbrains.kotlin.builtins.KotlinBuiltIns
import org.jetbrains.kotlin.descriptors.CallableDescriptor
import org.jetbrains.kotlin.descriptors.SimpleFunctionDescriptor
import org.jetbrains.kotlin.diagnostics.DiagnosticFactory3
import org.jetbrains.kotlin.diagnostics.DiagnosticSink
import org.jetbrains.kotlin.diagnostics.ParametrizedDiagnostic
import org.jetbrains.kotlin.js.patterns.DescriptorPredicate
import org.jetbrains.kotlin.js.patterns.PatternBuilder
import org.jetbrains.kotlin.js.resolve.diagnostics.ErrorsJs
import org.jetbrains.kotlin.psi.JetCallExpression
import org.jetbrains.kotlin.psi.JetExpression
import org.jetbrains.kotlin.psi.JetLiteralStringTemplateEntry
import org.jetbrains.kotlin.psi.JetStringTemplateExpression
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.BindingTrace
import org.jetbrains.kotlin.resolve.calls.checkers.CallChecker
import org.jetbrains.kotlin.resolve.calls.context.BasicCallResolutionContext
import org.jetbrains.kotlin.resolve.calls.model.ResolvedCall
import org.jetbrains.kotlin.resolve.constants.evaluate.ConstantExpressionEvaluator
import org.jetbrains.kotlin.types.JetType

import com.intellij.openapi.util.TextRange
import java.io.StringReader

import kotlin.platform.platformStatic

public class JsCallChecker : CallChecker {

    class object {
        public val JS_PATTERN: DescriptorPredicate = PatternBuilder.pattern("kotlin.js.js(String)")

        platformStatic
        public fun <F : CallableDescriptor?> ResolvedCall<F>.matchesJsCode(): Boolean {
            val descriptor = getResultingDescriptor()
            return descriptor is SimpleFunctionDescriptor && JS_PATTERN.apply(descriptor)
        }
    }

    override fun <F : CallableDescriptor?> check(resolvedCall: ResolvedCall<F>, context: BasicCallResolutionContext) {
        if (context.isAnnotationContext || !resolvedCall.matchesJsCode()) return

        val expression = resolvedCall.getCall().getCallElement()
        if (expression !is JetCallExpression) return

        val arguments = expression.getValueArgumentList()?.getArguments()
        val argument = arguments?.firstOrNull()?.getArgumentExpression()

        if (argument == null || !(checkArgumentIsStringLiteral(argument, context))) return

        checkSyntax(argument, context)
    }

    fun checkArgumentIsStringLiteral(
            argument: JetExpression,
            context: BasicCallResolutionContext
    ): Boolean {
        val stringType = KotlinBuiltIns.getInstance().getStringType()
        val evaluationResult = ConstantExpressionEvaluator.evaluate(argument, context.trace, stringType)

        if (evaluationResult == null) {
            context.trace.report(ErrorsJs.JSCODE_ARGUMENT_SHOULD_BE_LITERAL.on(argument))
        }

        return evaluationResult != null
    }

    fun checkSyntax(
            argument: JetExpression,
            context: BasicCallResolutionContext
    ): Boolean {
        val stringType = KotlinBuiltIns.getInstance().getStringType()
        val evaluationResult = ConstantExpressionEvaluator.evaluate(argument, context.trace, stringType)!!
        val code = evaluationResult.getValue() as String
        val reader = StringReader(code)

        val errorReporter = JsCodeErrorReporter(argument, code, context.trace)
        Context.enter().setErrorReporter(errorReporter)

        try {
            val ts = TokenStream(reader, "js", 0)
            val parser = Parser(IRFactory(ts), /* insideFunction = */ true)
            parser.parse(ts)
        } catch (e: AbortParsingException) {
            return false
        } finally {
            Context.exit()
        }

        return true
    }

}

class JsCodeErrorReporter(
        private val nodeToReport: JetExpression,
        private val code: String,
        private val trace: BindingTrace
) : ErrorReporter {
    override fun warning(message: String, startPosition: CodePosition, endPosition: CodePosition) {
        report(ErrorsJs.JSCODE_WARNING, message, startPosition, endPosition)
    }

    override fun error(message: String, startPosition: CodePosition, endPosition: CodePosition) {
        report(ErrorsJs.JSCODE_ERROR, message, startPosition, endPosition)
        throw AbortParsingException()
    }

    private fun report(
            diagnosticFactory: DiagnosticFactory3<JetExpression, String, String, List<TextRange>>,
            message: String,
            startPosition: CodePosition,
            endPosition: CodePosition
    ) {
        val helper: Helper = when {
            nodeToReport.isConstantStringLiteral -> StringLiteralHelper(message, startPosition, endPosition)
            else -> StringExpressionHelper(message, startPosition, endPosition)
        }

        val parametrizedDiagnostic = diagnosticFactory.on(nodeToReport, helper.plainTextMessage, helper.htmlMessage, helper.textRanges)
        trace.report(parametrizedDiagnostic)
    }

    private val CodePosition.absoluteOffset: Int
        get() {
            val quotesLength = nodeToReport.getFirstChild().getTextLength()
            return nodeToReport.getTextOffset() + quotesLength + code.offsetOf(this)
        }

    trait Helper {
        val plainTextMessage: String
        val htmlMessage: String
        val textRanges: List<TextRange>
    }

    inner class StringLiteralHelper(
            private val message: String,
            private val startPosition: CodePosition,
            private val endPosition: CodePosition
    ) : Helper {
        override val plainTextMessage: String = message
        override val htmlMessage: String = message
        override val textRanges: List<TextRange> = listOf(TextRange(startPosition.absoluteOffset, endPosition.absoluteOffset))
    }

    inner class StringExpressionHelper(
            private val message: String,
            startPosition: CodePosition,
            endPosition: CodePosition
    ) : Helper {
        private val start: Int = code.offsetOf(startPosition)
        private val end: Int = code.offsetOf(endPosition)

        override val plainTextMessage: String = "%s%nIn code:%n%s".format(message, code.underlineAsText(start, end))
        override val htmlMessage: String = "%s<br>In code:<br><pre>%s</pre>".format(message, code.underlineAsHtml(start, end))
        override val textRanges: List<TextRange> = listOf(nodeToReport.getTextRange())
    }
}

/**
 * Calculates an offset from the start of a text for a position,
 * defined by line and offset in that line.
 */
private fun String.offsetOf(position: CodePosition): Int {
    var i = 0
    var lineCount = 0
    var offsetInLine = 0

    while (i < length()) {
        val c = charAt(i)

        if (lineCount == position.line && offsetInLine == position.offset) {
            return i
        }

        i++
        offsetInLine++

        if (isEndOfLine(c.toInt())) {
            offsetInLine = 0
            lineCount++
            assert(lineCount <= position.line)
        }
    }

    return length()
}

private val JetExpression.isConstantStringLiteral: Boolean
    get() = this is JetStringTemplateExpression && getEntries().all { it is JetLiteralStringTemplateEntry }

/**
 * Underlines string in given rage.
 *
 * For example:
 * var  = 10;
 *    ^^^^
 */
private fun String.underlineAsText(from: Int, to: Int): String {
    val lines = StringBuilder()
    var marks = StringBuilder()
    var lineWasMarked = false

    for (i in indices) {
        val c = charAt(i)
        val mark: Char

        mark = when (i) {
            in from..to -> '^'
            else -> ' '
        }

        lines.append(c)
        marks.append(mark)
        lineWasMarked = lineWasMarked || mark != ' '

        if (isEndOfLine(c.toInt())) {
            if (lineWasMarked) {
                lines.appendln(marks.toString().trimTrailing())
                lineWasMarked = false
            }

            marks = StringBuilder()
        }
    }

    if (lineWasMarked) {
        lines.appendln()
        lines.append(marks.toString())
    }

    return lines.toString()
}

private fun String.underlineAsHtml(from: Int, to: Int): String {
    val lines = StringBuilder()
    var openMarker = false
    val underlineStart = "<u>"
    val underlineEnd = "</u>"

    for (i in indices) {
        val c = charAt(i)

        val mark = when (i) {
            from -> {
                openMarker = true
                underlineStart
            }
            to -> {
                openMarker = false
                underlineEnd
            }
            else -> ""
        }

        lines.append(mark)

        if (isEndOfLine(c.toInt()) && openMarker) {
            lines.append(underlineEnd + c + underlineStart)
        } else {
            lines.append(c)
        }
    }

    return lines.toString()
}

public object TestStringUtils {
    TestOnly
    public fun underlineAsText(string: String, from: Int, to: Int): String =
            string.underlineAsText(from, to)

    TestOnly
    public fun underlineAsHtml(string: String, from: Int, to: Int): String =
            string.underlineAsHtml(from, to)
}

