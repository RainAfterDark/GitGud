package org.gitgud.core.data;

import java.util.ArrayList;
import java.util.List;

public class Post {
    static final double VISUAL_BIAS = 4;

    public int x, y;           // World coordinates (Top-Left)
    public final int width, height;
    public final List<String> lines; // The wrapped text content

    public Post(String rawText) {
        this.lines = wrapTextIdeally(rawText);
        this.width = lines.stream().mapToInt(String::length).max().orElse(0) + 2;
        this.height = lines.size() + 2;
    }

    private List<String> wrapTextIdeally(String text) {
        int targetWidth = (int) Math.sqrt(text.length() * VISUAL_BIAS);
        if (targetWidth < 8) targetWidth = 8;

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
}
