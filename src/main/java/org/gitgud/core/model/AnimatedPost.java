package org.gitgud.core.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A post that can be animated with a set of frames.
 */
@BasePost
public class AnimatedPost extends Post {
    final List<List<String>> frames = new ArrayList<>();
    final double frameDuration;
    double frameTime = 0;
    int currentFrame = 0;

    private void advanceFrame() {
        lines = frames.get(currentFrame++);
        if (currentFrame >= frames.size()) currentFrame = 0;
        frameTime = 0;
    }

    /**
     * Creates a new animated post.
     * @param rawFrames a list of frames, each frame being a string.
     * @param frameDuration the duration of each frame, in seconds.
     */
    public AnimatedPost(List<String> rawFrames, double frameDuration) {
        this.frameDuration = frameDuration;
        if (rawFrames == null || rawFrames.isEmpty()) return;
        int maxWidth = 0, maxHeight = 0;
        for (String frame : rawFrames) {
            List<String> line = List.of(frame.split("\\n"));
            for (String s : line) {
                if (maxWidth < s.length()) maxWidth = s.length();
            }
            frames.add(line);
            if (maxHeight < line.size()) maxHeight = line.size();
        }
        setDimensions(maxWidth, maxHeight);
        advanceFrame();
    }

    @Override
    public void update(double deltaTIme) {
        if (frames.isEmpty()) return;
        frameTime += deltaTIme;
        if (frameTime >= frameDuration) advanceFrame();
    }
}
