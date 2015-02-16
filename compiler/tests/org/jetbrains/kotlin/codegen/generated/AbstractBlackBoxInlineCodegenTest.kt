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

package org.jetbrains.kotlin.codegen.generated

import org.jetbrains.kotlin.codegen.InlineTestUtil
import org.jetbrains.kotlin.utils.*

import java.io.File
import java.util.ArrayList
import org.jetbrains.kotlin.codegen.state.GenerationState
import com.google.common.collect.Lists
import org.jetbrains.kotlin.load.kotlin.PackageClassUtils
import org.jetbrains.kotlin.name.FqName
import com.intellij.openapi.util.io.FileUtil
import org.jetbrains.kotlin.psi.JetFile
import com.intellij.openapi.util.text.StringUtil
import java.util
import org.jetbrains.kotlin.test.InTextDirectivesUtils
import org.jetbrains.org.objectweb.asm.ClassVisitor
import com.intellij.openapi.util.Ref
import org.jetbrains.org.objectweb.asm.ClassReader
import org.jetbrains.org.objectweb.asm.Opcodes
import kotlin.test.assertEquals
import com.intellij.testFramework.UsefulTestCase
import org.junit.Assert
import org.jetbrains.kotlin.jvm.compiler.*
import org.jetbrains.kotlin.backend.common.output.*

public abstract class AbstractBlackBoxInlineCodegenTest : AbstractBlackBoxCodegenTest(), AbstractSMAPBaseTest {

    public fun doTestMultiFileWithInlineCheck(firstFileName: String) {
        var firstFileName = relativePath(File(firstFileName))
        val inputFiles = ArrayList<String>(2)
        inputFiles.add(firstFileName)
        inputFiles.add(firstFileName.substring(0, firstFileName.length() - "1.kt".length()) + "2.kt")

        doTestMultiFile(inputFiles)
        try {
            InlineTestUtil.checkNoCallsToInline(initializedClassLoader.getAllGeneratedFiles())
        }
        catch (e: Throwable) {
            System.out.println(generateToText())
            throw rethrow(e)
        }

        checkSMAP(myFiles.getPsiFiles(), generateClassesInFile().asList())
    }
}
