package com.example.regexvisualizer;

import org.fxmisc.richtext.InlineCssTextArea;

import java.util.Set;

public final class GraphicsHelper {

    /**
     * @param textAreaToModify An area of text to highlight -- corresponds to a GUI element
     * @param highlightIndices A set of indices that are currently highlighted -- can be changed later
     */
    public static void modifyTextArea(InlineCssTextArea textAreaToModify, Set<Integer> highlightIndices) {
        String textFromBox = textAreaToModify.getText();

        for(int i = 0; i < textFromBox.length(); i++) {
            if(highlightIndices.contains(i)) {
                textAreaToModify.setStyle(i, i + 1, "-fx-font-weight: bold");
            }
        }
    }


}
