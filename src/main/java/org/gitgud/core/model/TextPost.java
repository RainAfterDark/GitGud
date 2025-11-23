package org.gitgud.core.model;

import java.util.ArrayList;
import java.util.List;

@BasePost
public class TextPost extends Post {
    static final int WIDTH_BIAS = 4;
    static final int MIN_WIDTH = 8;

    public TextPost(String rawText, boolean wrap) {
        lines = wrap ? wrapText(rawText) : List.of(rawText.split("\\n"));
        calcDimensionsFromLines();
    }

    private List<String> wrapText(String text) {
        int targetWidth = (int) Math.sqrt(text.length() * WIDTH_BIAS);
        if (targetWidth < MIN_WIDTH) targetWidth = MIN_WIDTH;

        List<String> wrapped = getStrings(text, targetWidth);

        int maxWidth = 0;
        for(String s : wrapped) maxWidth = Math.max(maxWidth, s.length());
        for(int i=0; i<wrapped.size(); i++) {
            StringBuilder s = new StringBuilder(wrapped.get(i));
            while(s.length() < maxWidth) s.append(" ");
            wrapped.set(i, s.toString());
        }
        return wrapped;
    }

    private static List<String> getStrings(String text, int targetWidth) {
        List<String> wrapped = new ArrayList<>();
        String[] words = text.split("\\s+");
        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            if (currentLine.length() + 1 + word.length() > targetWidth) {
                if (!currentLine.isEmpty()) {
                    wrapped.add(currentLine.toString());
                    currentLine = new StringBuilder();
                }
            }
            if (!currentLine.isEmpty()) currentLine.append(" ");
            currentLine.append(word);
        }
        if (!currentLine.isEmpty()) wrapped.add(currentLine.toString());
        return wrapped;
    }

    @Override
    public void update(double deltaTIme) {}
}
