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

package org.jetbrains.kotlin.idea.ktSignature;

import com.intellij.codeInsight.ExternalAnnotationsManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.event.DocumentAdapter;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiModifierListOwner;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.kotlin.idea.util.BalloonWithEditor;
import org.jetbrains.kotlin.psi.JetFile;
import org.jetbrains.kotlin.resolve.AnalyzingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

class EditSignatureBalloon extends BalloonWithEditor {
    private final PsiModifierListOwner annotatedElement;
    private final String kotlinSignatureAnnotationFqName;

    public EditSignatureBalloon(
            @NotNull PsiModifierListOwner annotatedElement,
            @NotNull String previousSignature,
            boolean editable,
            @NotNull String kotlinSignatureAnnotationFqName
    ) {
        super(annotatedElement.getProject(), "Kotlin signature", previousSignature, editable);
        this.annotatedElement = annotatedElement;
        this.kotlinSignatureAnnotationFqName = kotlinSignatureAnnotationFqName;
    }

    @Override
    protected Editor createEditor() {
        Editor editor = super.createEditor();

        final Document document = editor.getDocument();
        document.addDocumentListener(new DocumentAdapter() {
            @Override
            public void documentChanged(DocumentEvent event) {
                PsiDocumentManager psiDocManager = PsiDocumentManager.getInstance(project);

                final PsiFile psiFile = psiDocManager.getPsiFile(document);
                assert psiFile instanceof JetFile;

                psiDocManager.performForCommittedDocument(document, new Runnable() {
                    @Override
                    public void run() {
                        ((MyPanel) panel).setSaveButtonEnabled(hasErrors((JetFile) psiFile));
                    }
                });
                psiDocManager.commitDocument(document);
            }
        }, this);

        return editor;
    }

    @Override
    protected ContentPanel createContentPanel() {
        return new MyPanel();
    }

    @Override
    protected void doRun() {
        new WriteCommandAction(project) {
            @Override
            protected void run(@NotNull Result result) throws Throwable {
                ExternalAnnotationsManager.getInstance(project).editExternalAnnotation(
                        annotatedElement, kotlinSignatureAnnotationFqName,
                        KotlinSignatureUtil.signatureToNameValuePairs(project, editor.getDocument().getText())
                );
            }
        }.execute();
    }

    private static boolean hasErrors(@NotNull JetFile file) {
        return AnalyzingUtils.getSyntaxErrorRanges(file).isEmpty();
    }

    private class MyPanel extends ContentPanel {
        private final JButton saveButton;

        MyPanel() {
            if (editable) {
                JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
                saveButton = new JButton("Save") {
                    @Override
                    public boolean isDefaultButton() {
                        return true;
                    }
                };

                toolbar.add(saveButton);
                add(toolbar, BorderLayout.SOUTH);

                ActionListener saveAndHideListener = new ActionListener() {
                    @Override
                    public void actionPerformed(@NotNull ActionEvent e) {
                        hideAndRun();
                    }
                };

                saveButton.addActionListener(saveAndHideListener);
                registerKeyboardAction(saveAndHideListener, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK),
                                       JComponent.WHEN_IN_FOCUSED_WINDOW);
            }
            else {
                saveButton = null;
            }
        }

        void setSaveButtonEnabled(boolean enabled) {
            if (saveButton != null) {
                saveButton.setEnabled(enabled);
                saveButton.setToolTipText(enabled ? null : "Please fix errors in signature to save it.");
            }
        }
    }
}
