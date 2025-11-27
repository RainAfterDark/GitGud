package org.gitgud.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A post that contains static text.
 */
@BasePost
public class StaticPost extends Post {
    static final int WIDTH_BIAS = 4;
    static final int MIN_WIDTH = 8;

    private static List<String> wrapText(String text) {
        int targetWidth = (int) Math.sqrt(text.length() * WIDTH_BIAS);
        if (targetWidth < MIN_WIDTH) targetWidth = MIN_WIDTH;

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

    /**
     * Creates a new static post.
     * @param rawText the message to display.
     * @param wrap whether to wrap the text to keep the post square.
     */
    public StaticPost(String rawText, boolean wrap) {
        lines = wrap ? wrapText(rawText) : List.of(rawText.split("\\n"));
        setDimensionsFromLines();
    }

    @Override
    public void update(double deltaTIme) {}
}
