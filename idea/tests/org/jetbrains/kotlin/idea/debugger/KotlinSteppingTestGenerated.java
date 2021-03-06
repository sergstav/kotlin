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

package org.jetbrains.kotlin.idea.debugger;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.InnerTestClasses;
import org.jetbrains.kotlin.test.JUnit3RunnerWithInners;
import org.jetbrains.kotlin.test.JetTestUtils;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.TestsPackage}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@InnerTestClasses({KotlinSteppingTestGenerated.StepInto.class, KotlinSteppingTestGenerated.SmartStepInto.class, KotlinSteppingTestGenerated.StepIntoOnly.class, KotlinSteppingTestGenerated.Filters.class})
@RunWith(JUnit3RunnerWithInners.class)
public class KotlinSteppingTestGenerated extends AbstractKotlinSteppingTest {
    @TestMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class StepInto extends AbstractKotlinSteppingTest {
        public void testAllFilesPresentInStepInto() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto"), Pattern.compile("^(.+)\\.kt$"), true);
        }

        @TestMetadata("classObjectFunFromClass.kt")
        public void testClassObjectFunFromClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/classObjectFunFromClass.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("classObjectFunFromTopLevel.kt")
        public void testClassObjectFunFromTopLevel() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/classObjectFunFromTopLevel.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("extFun.kt")
        public void testExtFun() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/extFun.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("javaFun.kt")
        public void testJavaFun() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/javaFun.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("memberFunFromClass.kt")
        public void testMemberFunFromClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/memberFunFromClass.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("memberFunFromTopLevel.kt")
        public void testMemberFunFromTopLevel() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/memberFunFromTopLevel.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("memberGetterFromClass.kt")
        public void testMemberGetterFromClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/memberGetterFromClass.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("memberGetterFromTopLevel.kt")
        public void testMemberGetterFromTopLevel() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/memberGetterFromTopLevel.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("objectFun.kt")
        public void testObjectFun() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/objectFun.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("topLevelFunFromClass.kt")
        public void testTopLevelFunFromClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/topLevelFunFromClass.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("topLevelFunFromTopLevel.kt")
        public void testTopLevelFunFromTopLevel() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/topLevelFunFromTopLevel.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("topLevelGetterFromClass.kt")
        public void testTopLevelGetterFromClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/topLevelGetterFromClass.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("topLevelGetterFromTopLevel.kt")
        public void testTopLevelGetterFromTopLevel() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/topLevelGetterFromTopLevel.kt");
            doStepIntoTest(fileName);
        }
    }

    @TestMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class SmartStepInto extends AbstractKotlinSteppingTest {
        public void testAllFilesPresentInSmartStepInto() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto"), Pattern.compile("^(.+)\\.kt$"), true);
        }

        @TestMetadata("classObjectFunFromClass.kt")
        public void testClassObjectFunFromClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/classObjectFunFromClass.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("classObjectFunFromTopLevel.kt")
        public void testClassObjectFunFromTopLevel() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/classObjectFunFromTopLevel.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("extFun.kt")
        public void testExtFun() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/extFun.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("javaFun.kt")
        public void testJavaFun() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/javaFun.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("memberFunFromClass.kt")
        public void testMemberFunFromClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/memberFunFromClass.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("memberFunFromTopLevel.kt")
        public void testMemberFunFromTopLevel() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/memberFunFromTopLevel.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("memberGetterFromClass.kt")
        public void testMemberGetterFromClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/memberGetterFromClass.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("memberGetterFromTopLevel.kt")
        public void testMemberGetterFromTopLevel() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/memberGetterFromTopLevel.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("objectFun.kt")
        public void testObjectFun() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/objectFun.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("topLevelFunFromClass.kt")
        public void testTopLevelFunFromClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/topLevelFunFromClass.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("topLevelFunFromTopLevel.kt")
        public void testTopLevelFunFromTopLevel() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/topLevelFunFromTopLevel.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("topLevelGetterFromClass.kt")
        public void testTopLevelGetterFromClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/topLevelGetterFromClass.kt");
            doSmartStepIntoTest(fileName);
        }

        @TestMetadata("topLevelGetterFromTopLevel.kt")
        public void testTopLevelGetterFromTopLevel() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepIntoAndSmartStepInto/topLevelGetterFromTopLevel.kt");
            doSmartStepIntoTest(fileName);
        }
    }

    @TestMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepInto")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class StepIntoOnly extends AbstractKotlinSteppingTest {
        @TestMetadata("accessors.kt")
        public void testAccessors() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepInto/accessors.kt");
            doStepIntoTest(fileName);
        }

        public void testAllFilesPresentInStepIntoOnly() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/debugger/tinyApp/src/stepInto/stepInto"), Pattern.compile("^(.+)\\.kt$"), true);
        }

        @TestMetadata("continueLabel.kt")
        public void testContinueLabel() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepInto/continueLabel.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("forLoop.kt")
        public void testForLoop() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepInto/forLoop.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("returnVoid.kt")
        public void testReturnVoid() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepInto/returnVoid.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("syntheticMethods.kt")
        public void testSyntheticMethods() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepInto/syntheticMethods.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("whenExpr.kt")
        public void testWhenExpr() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/stepInto/stepInto/whenExpr.kt");
            doStepIntoTest(fileName);
        }
    }

    @TestMetadata("idea/testData/debugger/tinyApp/src/filters")
    @TestDataPath("$PROJECT_ROOT")
    @RunWith(JUnit3RunnerWithInners.class)
    public static class Filters extends AbstractKotlinSteppingTest {
        public void testAllFilesPresentInFilters() throws Exception {
            JetTestUtils.assertAllTestsPresentByMetadata(this.getClass(), new File("idea/testData/debugger/tinyApp/src/filters"), Pattern.compile("^(.+)\\.kt$"), true);
        }

        @TestMetadata("checkNotNull.kt")
        public void testCheckNotNull() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/filters/checkNotNull.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("doNotSkipClassloader.kt")
        public void testDoNotSkipClassloader() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/filters/doNotSkipClassloader.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("doNotSkipConstructors.kt")
        public void testDoNotSkipConstructors() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/filters/doNotSkipConstructors.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("npe.kt")
        public void testNpe() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/filters/npe.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("reflectKClass.kt")
        public void testReflectKClass() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/filters/reflectKClass.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("skipClassloader.kt")
        public void testSkipClassloader() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/filters/skipClassloader.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("skipConstructors.kt")
        public void testSkipConstructors() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/filters/skipConstructors.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("stdlibStep.kt")
        public void testStdlibStep() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/filters/stdlibStep.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("stepIntoSpecificKotlinClasses.kt")
        public void testStepIntoSpecificKotlinClasses() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/filters/stepIntoSpecificKotlinClasses.kt");
            doStepIntoTest(fileName);
        }

        @TestMetadata("stepIntoStdlib.kt")
        public void testStepIntoStdlib() throws Exception {
            String fileName = JetTestUtils.navigationMetadata("idea/testData/debugger/tinyApp/src/filters/stepIntoStdlib.kt");
            doStepIntoTest(fileName);
        }
    }
}
