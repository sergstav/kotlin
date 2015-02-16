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

import org.jetbrains.kotlin.backend.common.output.*
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.load.kotlin.*
import org.jetbrains.kotlin.name.*
import com.intellij.openapi.util.io.*
import com.intellij.openapi.util.*
import org.jetbrains.org.objectweb.asm.*
import org.jetbrains.kotlin.test.*
import org.junit.*

public trait AbstractSMAPBaseTest {

    private fun extractSMAPFromClasses(outputFiles: List<OutputFile>): List<SMAPAndFile> {
        val result = arrayListOf<SMAPAndFile>()
        for (outputFile in outputFiles) {
            if (PackageClassUtils.isPackageClassFqName(FqName(FileUtil.getNameWithoutExtension(outputFile.relativePath).replace('/', '.')))) {
                // Don't test line numbers in *Package facade classes
                continue
            }
            val debugInfo = Ref<String?>()
            ClassReader(outputFile.asByteArray()).accept(object: ClassVisitor(Opcodes.ASM5) {
                override fun visitSource(source: String?, debug: String?) {
                    debugInfo.set(debug)
                }
            }, 0)
            val sourceFiles = outputFile.sourceFiles
            assert(sourceFiles.size() == 1, "Wrong number of source files ${sourceFiles.size()}")
            result.add(SMAPAndFile(debugInfo.get(), FileUtil.toSystemIndependentName(sourceFiles.first().getAbsolutePath())))
        }

        return result
    }

    private fun extractSmapFromSource(file: JetFile): String? {
        val fileContent = file.getText();
        val smapPrefix = "//SMAP"
        if (InTextDirectivesUtils.isDirectiveDefined(fileContent, smapPrefix)) {
            InTextDirectivesUtils.findLinesWithPrefixesRemoved(fileContent, smapPrefix)
            var smapData = fileContent.substring(fileContent.indexOf(smapPrefix))
            smapData = smapData.replaceAll("//","").trim()
            if (smapData.startsWith("SMAP ABSENT")) {
                return null
            }
            return smapData
        }
        return null;
    }

    fun checkSMAP(inputFiles: List<JetFile>, outputFiles: List<OutputFile>) {
        val sourceData = arrayListOf<SMAPAndFile>()
        for (file: JetFile in inputFiles) {
            val smap = extractSmapFromSource(file)
            if (smap != null) {
                sourceData.add(SMAPAndFile(smap, FileUtil.toSystemIndependentName(file.getVirtualFile().getCanonicalPath()!!)))
            }
        }
        val compiledData = extractSMAPFromClasses(outputFiles).groupBy {
            it.sourceFile
        }.map { it ->
            SMAPAndFile(it.getValue().fold(null)
                                            {   (a, e): String? ->
                                                    if (a == null) e.smap
                                                    else if (e.smap == null) a
                                                    else "$a\n${e.smap}"
                                            },
                        it.key)
        }.toMap { it.sourceFile }

        for (source in sourceData) {
            val data = compiledData[source.sourceFile]
            Assert.assertEquals("Smap data differs for ${source.sourceFile}", source.smap, replaceHash(data?.smap?.trim()))
        }
    }

    fun replaceHash(data: String?): String? {
        if (data == null) return null

        val fileSectionStart = data.indexOf("*F") + 3
        val lineSection = data.indexOf("*L") - 1

        val files: Array<String> = data.substring(fileSectionStart, lineSection).split("\n")

        val cleaned = files.map {
            val shortName = it.substring(it.lastIndexOf("/") + 1)
            if (shortName.contains("Package$") && shortName.count {it == '$'} > 2) {
                val hash = it.substringAfter("$").substringAfter("$").substringBefore("$")
                it.replace(hash, "HASH")
            } else {
                it
            }
        }.join("\n")

        return data.substring(0, fileSectionStart) + cleaned + data.substring(lineSection)
    }

    class SMAPAndFile(val smap: String?, val sourceFile: String)
}