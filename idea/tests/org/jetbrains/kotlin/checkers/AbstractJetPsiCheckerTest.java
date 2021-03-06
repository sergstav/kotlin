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

package org.jetbrains.kotlin.checkers;

import com.intellij.rt.execution.junit.FileComparisonFailure;
import com.intellij.spellchecker.inspections.SpellCheckingInspection;
import com.intellij.testFramework.LightProjectDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.idea.JetLightCodeInsightFixtureTestCase;
import org.jetbrains.kotlin.idea.highlighter.JetPsiChecker;

import java.io.File;

public abstract class AbstractJetPsiCheckerTest extends JetLightCodeInsightFixtureTestCase {
    public void doTest(@NotNull String filePath) throws Exception {
        myFixture.configureByFile(filePath);
        checkHighlighting(true, false, false);
    }

    public void doTestWithInfos(@NotNull String filePath) throws Exception {
        try {
            myFixture.configureByFile(filePath);

            //noinspection unchecked
            myFixture.enableInspections(SpellCheckingInspection.class);

            JetPsiChecker.Default.setNamesHighlightingEnabled(false);
            checkHighlighting(true, true, false);
        }
        finally {
            JetPsiChecker.Default.setNamesHighlightingEnabled(true);
        }
    }

    protected long checkHighlighting(boolean checkWarnings, boolean checkInfos, boolean checkWeakWarnings) {
        try {
            return myFixture.checkHighlighting(checkWarnings, checkInfos, checkWeakWarnings);
        }
        catch (FileComparisonFailure e) {
            throw new FileComparisonFailure(e.getMessage(), e.getExpected(), e.getActual(), new File(e.getFilePath()).getAbsolutePath());
        }
    }

    @NotNull
    @Override
    protected LightProjectDescriptor getProjectDescriptor() {
        return getProjectDescriptorFromTestName();
    }
}
