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

package org.jetbrains.kotlin.idea.util;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.Result;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.editor.colors.EditorColors;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.EditorGutterComponentEx;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.Balloon;
import com.intellij.openapi.ui.popup.JBPopupAdapter;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.psi.PsiElement;
import com.intellij.testFramework.LightVirtualFile;
import com.intellij.ui.awt.RelativePoint;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.kotlin.idea.JetFileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class BalloonWithEditor implements Disposable {
    protected final Editor editor;
    protected final Project project;
    protected final ContentPanel panel;
    protected final Balloon balloon;
    protected final String initialText;
    protected final boolean editable;

    public BalloonWithEditor(@NotNull Project project, @NotNull String title, @NotNull String initialText, boolean editable) {
        this.project = project;
        this.editable = editable;
        this.initialText = initialText;
        editor = createEditor();
        panel = createContentPanel();
        balloon = createBalloon(title);
    }

    private Balloon createBalloon(String title) {
        Balloon balloon = JBPopupFactory.getInstance().createDialogBalloonBuilder(panel, title)
                .setHideOnClickOutside(true)
                .setHideOnKeyOutside(true)
                .setBlockClicksThroughBalloon(true).createBalloon();

        balloon.addListener(new JBPopupAdapter() {
            @Override
            public void onClosed(LightweightWindowEvent event) {
                Disposer.dispose(BalloonWithEditor.this);
            }
        });
        return balloon;
    }

    protected Editor createEditor() {
        EditorFactory editorFactory = EditorFactory.getInstance();
        assert editorFactory != null;
        LightVirtualFile virtualFile = new LightVirtualFile("dummy.kt", JetFileType.INSTANCE, initialText);
        virtualFile.setWritable(editable);
        Document document = FileDocumentManager.getInstance().getDocument(virtualFile);
        assert document != null;

        Editor editor = editorFactory.createEditor(document, project, JetFileType.INSTANCE, !editable);
        EditorSettings settings = editor.getSettings();
        settings.setVirtualSpace(false);
        settings.setLineMarkerAreaShown(false);
        settings.setFoldingOutlineShown(false);
        settings.setRightMarginShown(false);
        settings.setAdditionalPageAtBottom(false);
        settings.setAdditionalLinesCount(2);
        settings.setAdditionalColumnsCount(12);

        assert editor instanceof EditorEx;
        ((EditorEx)editor).setEmbeddedIntoDialogWrapper(true);

        editor.getColorsScheme().setColor(EditorColors.CARET_ROW_COLOR, editor.getColorsScheme().getDefaultBackground());

        return editor;
    }

    private static int getLineY(@NotNull Editor editor, @NotNull PsiElement psiElementInEditor) {
        LogicalPosition logicalPosition = editor.offsetToLogicalPosition(psiElementInEditor.getTextOffset());
        return editor.logicalPositionToXY(logicalPosition).y;
    }

    protected final void hideAndRun() {
        balloon.hide();

        if (initialText.equals(editor.getDocument().getText())) return;

        new WriteCommandAction(project) {
            @Override
            protected void run(@NotNull Result result) throws Throwable {
                doRun();
            }
        }.execute();
    }

    protected void doRun() {

    }

    protected ContentPanel createContentPanel() {
        return new ContentPanel();
    }

    public final Editor getEditor() {
        return editor;
    }

    public final void show(
            @Nullable Point point,
            @NotNull final Editor mainEditor,
            @NotNull PsiElement psiElementInEditor,
            @NotNull Balloon.Position position
    ) {
        int lineY = getLineY(mainEditor, psiElementInEditor);
        EditorGutterComponentEx gutter = (EditorGutterComponentEx) mainEditor.getGutter();
        Point adjustedPoint;
        if (point == null) {
            adjustedPoint = new Point(gutter.getIconsAreaWidth() + gutter.getLineMarkerAreaOffset(), lineY);
        }
        else {
            adjustedPoint = new Point(point.x, Math.min(lineY, point.y));
        }
        balloon.show(new RelativePoint(gutter, adjustedPoint), position);
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                IdeFocusManager.getInstance(mainEditor.getProject()).requestFocus(editor.getContentComponent(), false);
            }
        });
    }

    public final void close() {
        balloon.hide(true);
    }

    @Override
    public final void dispose() {
        EditorFactory editorFactory = EditorFactory.getInstance();
        assert editorFactory != null;
        editorFactory.releaseEditor(editor);
    }

    public final boolean isDisposed() {
        return balloon.isDisposed();
    }

    protected class ContentPanel extends JPanel {
        protected ContentPanel() {
            super(new BorderLayout());
            add(editor.getComponent(), BorderLayout.CENTER);

            registerKeyboardAction(new ActionListener() {
                @Override
                public void actionPerformed(@NotNull ActionEvent e) {
                    balloon.hide();
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
        }
    }
}
