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

package org.jetbrains.kotlin.jvm.compiler

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.io.FileUtil
import com.intellij.util.ArrayUtil
import org.jetbrains.kotlin.backend.common.output.OutputFile
import org.jetbrains.kotlin.backend.common.output.OutputFileCollection
import org.jetbrains.kotlin.cli.common.output.outputUtils.*
import org.jetbrains.kotlin.cli.jvm.compiler.EnvironmentConfigFiles
import org.jetbrains.kotlin.cli.jvm.compiler.JetCoreEnvironment
import org.jetbrains.kotlin.codegen.ClassFileFactory
import org.jetbrains.kotlin.codegen.GenerationUtils
import org.jetbrains.kotlin.codegen.InlineTestUtil
import org.jetbrains.kotlin.codegen.forTestCompile.ForTestCompileRuntime
import org.jetbrains.kotlin.config.CompilerConfiguration
import org.jetbrains.kotlin.load.kotlin.PackageClassUtils
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.psi.JetFile
import org.jetbrains.kotlin.test.ConfigurationKind
import org.jetbrains.kotlin.test.JetTestUtils
import org.jetbrains.kotlin.test.TestCaseWithTmpdir
import org.jetbrains.kotlin.test.TestJdkKind
import org.jetbrains.kotlin.utils.*

import java.io.File
import java.io.IOException
import java.lang.reflect.Method
import java.net.URL
import java.net.URLClassLoader
import java.util.ArrayList
import java.util.Collections

public abstract class AbstractCompileKotlinAgainstInlineKotlinTest : AbstractCompileKotlinAgainstKotlinTest(), AbstractSMAPBaseTest {

    throws(javaClass<Exception>())
    public fun doBoxTestWithInlineCheck(firstFileName: String) {
        val inputFiles = ArrayList<String>(2)
        inputFiles.add(firstFileName)
        inputFiles.add(firstFileName.substring(0, firstFileName.length() - "1.kt".length()) + "2.kt")

        val (factory1, factory2) = doBoxTest(inputFiles)
        val allGeneratedFiles = ArrayList(factory1.asList())
        allGeneratedFiles.addAll(factory2.asList())
        InlineTestUtil.checkNoCallsToInline(allGeneratedFiles)

        val sourceFiles = ArrayList(factory1.getInputFiles())
        sourceFiles.addAll(factory2.getInputFiles())
        checkSMAP(sourceFiles, allGeneratedFiles)
    }

    throws(javaClass<Exception>())
    private fun doBoxTest(files: List<String>): Pair<ClassFileFactory, ClassFileFactory> {
        Collections.sort<String>(files)

        var factory1: ClassFileFactory? = null
        var factory2: ClassFileFactory? = null
        try {
            factory1 = compileA(File(files.get(1))) as ClassFileFactory
            factory2 = compileB(File(files.get(0))) as ClassFileFactory
            invokeBox()
        }
        catch (e: Throwable) {
            var result = ""
            if (factory1 != null) {
                result += "FIRST: \n\n" + factory1!!.createText()
            }
            if (factory2 != null) {
                result += "\n\nSECOND: \n\n" + factory2!!.createText()
            }
            System.out.println(result)
            throw rethrow(e)
        }

        return Pair(factory1!!, factory2!!)
    }

}
