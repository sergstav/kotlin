/*
 * Copyright 2010-2014 JetBrains s.r.o.
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

package org.jetbrains.jet.lang.resolve

import org.jetbrains.jet.lang.resolve.lazy.KotlinCodeAnalyzer
import kotlin.platform.platformStatic
import com.intellij.openapi.project.Project
import com.intellij.openapi.components.ServiceManager
import org.jetbrains.jet.lang.descriptors.ModuleDescriptor
import javax.inject.Inject
import kotlin.properties.Delegates
import javax.annotation.PostConstruct

public open class AnalyzeSessionProvider(protected val project: Project) {
    class object {
        platformStatic
        public fun getInstance(project: Project): AnalyzeSessionProvider {
            return ServiceManager.getService<AnalyzeSessionProvider>(project, javaClass<AnalyzeSessionProvider>())!!
        }
    }

    public open fun initialize(trace: BindingTrace, module: ModuleDescriptor, codeAnalyzer: KotlinCodeAnalyzer?) {
        // Do nothing by default
    }

    public open fun createTrace(): BindingTraceContext = BindingTraceContext()
}

public open class AnalyzerPostConstruct {
    PostConstruct public open fun postCreate() {}
}