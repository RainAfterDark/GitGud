package org.gitgud.core.algo;

import java.util.Iterator;

public class SpiralIterator implements Iterator<int[]> {
    static final int HORIZONTAL_BIAS = 3;

    int x = 0, y = 0;
    int dx = 0, dy = -1; // Start moving UP
    int stepCount = 0;
    int segmentLength = 1; // Base length of the current spiral arm (before bias)
    int turnCount = 0;

    @Override
    public boolean hasNext() { return true; }

    @Override
    public int[] next() {
        int[] current = {x, y};
        x += dx;
        y += dy;
        stepCount++;

        // Determine the limit for the current direction
        // If dx != 0, we are moving horizontally, so we apply the bias.
        int currentLimit = (dx != 0) ? (segmentLength * HORIZONTAL_BIAS) : segmentLength;

        if (stepCount >= currentLimit) {
            stepCount = 0;
            turnCount++;

            // Rotate 90 degrees: (0,-1) -> (1,0) -> (0,1) -> (-1,0)
            int temp = dx;
            dx = -dy;
            dy = temp;

            // Every 2 turns (one vertical leg + one horizontal leg), the base spiral grows
            if (turnCount % 2 == 0) {
                segmentLength++;
            }
        }
        return current;
    }
}
