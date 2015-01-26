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

import java.io.File
import org.jetbrains.kotlin.utils.fileUtils.*
import org.jetbrains.kotlin.resolve.diagnostics.*

import org.junit.Test
import org.junit.Assert
import org.jetbrains.kotlin.js.resolve.diagnostics.underlineAsText
import org.jetbrains.kotlin.js.resolve.diagnostics.underlineAsHtml
import junit.framework.TestCase

public abstract class AbstractJsCallCheckerTest : TestCase() {

    class object {
        public val TEST_EXTENSION: String = "test"
        private val EXPECT_EXTENSION = "expect"
        private val UNDERLINE = "@"
    }

    public fun doUnderlinePlainTextTest(testPath: String): Unit =
            doUnderlineTest(testPath, String::underlineAsText)

    public fun doUnderlineHtmlTest(testPath: String): Unit =
            doUnderlineTest(testPath, String::underlineAsHtml)

    private fun doUnderlineTest(testPath: String, underline: String.(Int, Int)->String) {
        val testFile = File(testPath)
        val expectFile = File(testPath.expected)

        val test = testFile.readTextOrEmpty()
        val expected = expectFile.readTextOrEmpty()
        val testWithoutTags = test.replaceAll(UNDERLINE, "")

        val underlineTags = test.indices.filter { test.charAt(it).toString() == UNDERLINE }
        assert(underlineTags.size() >= 2) { "not enough underline tags" }
        val start = underlineTags.get(0)
        val end = underlineTags.get(1) - UNDERLINE.length()

        val actual = testWithoutTags.underline(start, end)
        Assert.assertEquals(expected.trim(), actual.trim())
    }

    private val String.expected: String
        get() = substring(0, length() - TEST_EXTENSION.length()) + EXPECT_EXTENSION
}
