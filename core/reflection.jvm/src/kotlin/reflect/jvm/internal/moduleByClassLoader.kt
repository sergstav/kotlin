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

package kotlin.reflect.jvm.internal

import java.util.concurrent.ConcurrentMap
import kotlin.reflect.jvm.internal.containers.CustomConcurrentHashMap
import kotlin.reflect.jvm.internal.containers.CustomConcurrentHashMap.*
import org.jetbrains.kotlin.load.kotlin.reflect.RuntimeModuleData

private val moduleByClassLoader: ConcurrentMap<ClassLoader, RuntimeModuleData> =
        CustomConcurrentHashMap(WEAK, IDENTITY, WEAK, IDENTITY, 0)

private fun Class<*>.getOrCreateModule(): RuntimeModuleData {
    val classLoader = this.getClassLoader() ?: ClassLoader.getSystemClassLoader()

    val cached = moduleByClassLoader[classLoader]
    if (cached != null) return cached

    val module = RuntimeModuleData.create(classLoader)
    moduleByClassLoader.putIfAbsent(classLoader, module)
    return module
}
