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

package org.jetbrains.kotlin.idea.caches.resolve

import org.jetbrains.kotlin.resolve.lazy.ResolveSession
import org.jetbrains.kotlin.analyzer.ResolverForModule
import org.jetbrains.kotlin.analyzer.PlatformAnalysisParameters
import org.jetbrains.kotlin.analyzer.AnalyzerFacade
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.context.GlobalContext
import org.jetbrains.kotlin.analyzer.ResolverForProject
import org.jetbrains.kotlin.js.analyze.TopDownAnalyzerFacadeForJS
import org.jetbrains.kotlin.platform.PlatformToKotlinClassMap
import org.jetbrains.kotlin.resolve.lazy.declarations.DeclarationProviderFactoryService
import org.jetbrains.kotlin.resolve.BindingTraceContext
import org.jetbrains.kotlin.di.InjectorForLazyResolve
import org.jetbrains.kotlin.descriptors.impl.ModuleDescriptorImpl
import org.jetbrains.kotlin.analyzer.ModuleInfo
import org.jetbrains.kotlin.analyzer.ModuleContent
import org.jetbrains.kotlin.js.resolve.KotlinJsCheckerProvider
import org.jetbrains.kotlin.types.DynamicTypesAllowed
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException
import org.jetbrains.kotlin.utils.LibraryUtils
import java.io.InputStream
import java.io.ByteArrayInputStream
import java.util.ArrayList
import org.jetbrains.kotlin.descriptors.PackageFragmentProvider
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.builtins.BuiltinsPackageFragment
import org.jetbrains.kotlin.storage.LockBasedStorageManager
import org.jetbrains.kotlin.descriptors.impl.CompositePackageFragmentProvider
import com.intellij.openapi.vfs.VirtualFileVisitor
import com.intellij.openapi.util.text.StringUtil
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.util.PathUtil
import org.jetbrains.kotlin.js.KotlinJavascriptPackageFragment

public class JsResolverForModule(
        override val lazyResolveSession: ResolveSession
) : ResolverForModule


public object JsAnalyzerFacade : AnalyzerFacade<JsResolverForModule, PlatformAnalysisParameters> {

    override fun <M : ModuleInfo> createResolverForModule(
            moduleInfo: M,
            project: Project,
            globalContext: GlobalContext,
            moduleDescriptor: ModuleDescriptorImpl,
            moduleContent: ModuleContent,
            platformParameters: PlatformAnalysisParameters,
            resolverForProject: ResolverForProject<M, JsResolverForModule>
    ): JsResolverForModule {
        val (syntheticFiles, moduleContentScope) = moduleContent
        val declarationProviderFactory = DeclarationProviderFactoryService.createDeclarationProviderFactory(
                project, globalContext.storageManager, syntheticFiles, moduleContentScope
        )

        val injector = InjectorForLazyResolve(project, globalContext, moduleDescriptor, declarationProviderFactory, BindingTraceContext(),
                                              KotlinJsCheckerProvider, DynamicTypesAllowed())
        val resolveSession = injector.getResolveSession()!!
        var packageFragmentProvider = resolveSession.getPackageFragmentProvider()

        if (moduleInfo is LibraryInfo) {
            val files = moduleInfo.library.getFiles(OrderRootType.CLASSES)
            val metadataList: MutableList<LibraryUtils.Metadata> = arrayListOf()
            for(file in files) {
                metadataList.addAll(LibraryUtils.loadMetadataFromLibrary(PathUtil.getLocalPath(file)))
            }
            if (metadataList.size() > 0) {
                val providers = ArrayList<PackageFragmentProvider>()
                providers.add(packageFragmentProvider)
                for (metadata in metadataList) {
                    loadModuleDescriptors(moduleDescriptor, metadata, providers)
                }
                packageFragmentProvider = CompositePackageFragmentProvider(providers)
            }
        }
        moduleDescriptor.initialize(packageFragmentProvider)
        return JsResolverForModule(resolveSession)
    }

    private fun loadModuleDescriptors(moduleDescriptor: ModuleDescriptorImpl, metadata: LibraryUtils.Metadata, providers: MutableList<PackageFragmentProvider>) {
        val descriptors = metadata.files
        val load = { (path: String) -> if (!descriptors.containsKey(path)) null else ByteArrayInputStream(descriptors.get(path)) }

        for (packageName in metadata.packages) {
            val fqName = FqName(packageName)
            val packageFragment = KotlinJavascriptPackageFragment(fqName, LockBasedStorageManager(), moduleDescriptor, load)
            providers.add(packageFragment.provider)
        }
    }

    override val defaultImports = TopDownAnalyzerFacadeForJS.DEFAULT_IMPORTS
    override val platformToKotlinClassMap = PlatformToKotlinClassMap.EMPTY

}
