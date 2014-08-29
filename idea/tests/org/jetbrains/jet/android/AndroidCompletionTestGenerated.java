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

package org.jetbrains.jet.android;

import org.jetbrains.jet.JetTestUtils;
import org.jetbrains.jet.test.TestMetadata;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.jet.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("idea/testData/android/completion")
public class AndroidCompletionTestGenerated extends AbstractAndroidCompletionTest {
    public void SKIPtestAllFilesPresentInCompletion() throws Exception {
        JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), "org.jetbrains.jet.generators.tests.TestsPackage", new File("idea/testData/android/completion"), Pattern.compile("^([^\\.]+)$"), false);
    }
    
    @TestMetadata("fqNameInAttr")
    public void testFqNameInAttr() throws Exception {
        doTest("idea/testData/android/completion/fqNameInAttr/");
    }
    
    @TestMetadata("fqNameInTag")
    public void testFqNameInTag() throws Exception {
        doTest("idea/testData/android/completion/fqNameInTag/");
    }
    
    @TestMetadata("multiFile")
    public void testMultiFile() throws Exception {
        doTest("idea/testData/android/completion/multiFile/");
    }
    
    @TestMetadata("propertiesSimple")
    public void testPropertiesSimple() throws Exception {
        doTest("idea/testData/android/completion/propertiesSimple/");
    }
    
}
