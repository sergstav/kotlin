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

package org.jetbrains.kotlin.idea.imports

import com.intellij.lang.ImportOptimizer
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.progress.ProgressIndicatorProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.jetbrains.kotlin.psi.*
import org.jetbrains.kotlin.name.*
import org.jetbrains.kotlin.idea.references.JetReference
import org.jetbrains.kotlin.idea.util.ImportInsertHelper
import java.util.*
import org.jetbrains.kotlin.resolve.descriptorUtil.*
import org.jetbrains.kotlin.descriptors.*
import org.jetbrains.kotlin.idea.formatter.JetCodeStyleSettings
import org.jetbrains.kotlin.resolve.*
import org.jetbrains.kotlin.psi.psiUtil.*

public class KotlinImportOptimizer() : ImportOptimizer {

    override fun supports(file: PsiFile?) = file is JetFile

    override fun processFile(file: PsiFile?) = Runnable() {
        OptimizeProcess(file as JetFile).execute()
    }

    private class OptimizeProcess(val file: JetFile) {
        private val codeStyleSettings = JetCodeStyleSettings.getInstance(file.getProject())
        private val aliasImports: Map<Name, FqName>

        ;{
            val imports = file.getImportDirectives()
            val aliasImports = HashMap<Name, FqName>()
            for (import in imports) {
                val path = import.getImportPath() ?: continue
                val aliasName = path.getAlias()
                if (aliasName != null) {
                    aliasImports.put(aliasName, path.fqnPart())
                }
                else {
                    //TODO: detect other "special" imports
                }
            }
            this.aliasImports = aliasImports
        }

        public fun execute() {
            //TODO: keep existing imports? at least aliases (comments)

            val importInsertHelper = ImportInsertHelper.getInstance(file.getProject())
            val currentPackageName = file.getPackageFqName()

            val descriptorsToImport = detectDescriptorsToImport()

            val importsToGenerate = ArrayList<ImportPath>()
            if (codeStyleSettings.PREFER_ALL_UNDER_IMPORTS) {
                //TODO
                //TODO: don't forget that packages may not be imported by *
            }
            else {
                for (descriptor in descriptorsToImport) {
                    val fqName = descriptor.importableFqNameSafe
                    if (descriptor !is PackageViewDescriptor && fqName.parent() == currentPackageName) continue
                    val importPath = ImportPath(fqName, false)
                    if (importInsertHelper.isImportedWithDefault(importPath, file)/*TODO: implementation is not quite correct*/) continue
                    importsToGenerate.add(importPath)
                }
            }

            //TODO: drop unused aliases
            aliasImports.mapTo(importsToGenerate) { ImportPath(it.getValue(), false, it.getKey())}

            val sortedImportsToGenerate = importsToGenerate.sortBy(importInsertHelper.importSortComparator)

            //TODO: do not touch file if everything is already correct?

            ApplicationManager.getApplication()!!.runWriteAction(Runnable {
                for (import in file.getImportDirectives()) {
                    import.delete()
                }

                val importList = file.getImportList()!!
                val psiFactory = JetPsiFactory(file.getProject())
                for (importPath in sortedImportsToGenerate) {
                    importList.add(psiFactory.createImportDirective(importPath))
                }
            })
        }

        private fun detectDescriptorsToImport(): Set<DeclarationDescriptor> {
            val usedDescriptors = HashSet<DeclarationDescriptor>()
            file.accept(object : JetVisitorVoid() {
                override fun visitElement(element: PsiElement) {
                    ProgressIndicatorProvider.checkCanceled()
                    element.acceptChildren(this)
                }

                override fun visitImportList(importList: JetImportList) {
                }

                override fun visitPackageDirective(directive: JetPackageDirective) {
                }

                override fun visitJetElement(element: JetElement) {
                    val reference = element.getReference()
                    if (reference is JetReference) {
                        val targets = reference.resolveToDescriptors()
                        //TODO: check if it's resolved through alias
                        for (target in targets) {
                            if (!target.canBeReferencedViaImport()) continue
                            if (target is PackageViewDescriptor && target.getFqName().parent() == FqName.ROOT) continue // no need to import top-level packages (TODO: is that always true?)

                            if (!target.isExtension) { // for non-extension targets, count only non-qualified simple name usages
                                if (element !is JetNameReferenceExpression) continue
                                if (element.getIdentifier() == null) continue // skip 'this' etc
                                if (element.getReceiverExpression() != null) continue
                            }

                            if (isAccessibleAsMember(target, element)) continue

                            usedDescriptors.add(target.getImportableDescriptor())
                        }
                    }

                    super.visitJetElement(element)
                }
            })
            return usedDescriptors
        }

        private fun isAccessibleAsMember(target: DeclarationDescriptor, place: JetElement): Boolean {
            val container = target.getContainingDeclaration()
            if (container !is ClassDescriptor) return false
            val scope = if (container.getKind() == ClassKind.CLASS_OBJECT)
                container.getContainingDeclaration() as? ClassDescriptor ?: return false
            else
                container
            val declaration = DescriptorToSourceUtils.classDescriptorToDeclaration(scope)
            return declaration != null && declaration.getContainingFile() == file && declaration.isAncestor(place)
        }
    }
}
