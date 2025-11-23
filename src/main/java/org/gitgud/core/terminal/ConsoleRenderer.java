package org.gitgud.core.terminal;

import org.gitgud.core.model.Post;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.AttributedString;
import org.jline.utils.Display;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsoleRenderer {
    static final int TARGET_FPS = 60;
    static final char HL = '─';
    static final char VL = '│';
    static final char TL = '┌', TR = '┐', BL = '└', BR = '┘';

    public final AtomicInteger camX = new AtomicInteger(0);
    public final AtomicInteger camY = new AtomicInteger(0);
    public final AtomicBoolean running = new AtomicBoolean(true);

    public final Terminal terminal;
    final Display display;
    final List<Post> posts;

    char[][] buffer;
    int width = 0;
    int height = 0;
    double deltaTime = 0;

    public ConsoleRenderer(List<Post> posts) throws IOException {
        terminal = TerminalBuilder.builder().system(true).build();
        terminal.enterRawMode();
        display = new Display(terminal, true);
        this.posts = posts;
    }

    private void handleResize() {
        int newWidth = terminal.getWidth();
        int newHeight = terminal.getHeight();

        if (newWidth != width || newHeight != height) {
            width = newWidth;
            height = newHeight;
            buffer = new char[newHeight][newWidth];

            display.resize(newHeight, newWidth);
            terminal.puts(InfoCmp.Capability.clear_screen);
        }
    }

    private void clearBuffer() {
        for (char[] row : buffer)
            Arrays.fill(row, ' ');
    }

    private static char getCharacterAt(Post p, int y, int x) {
        char c = ' ';
        if (y == 0) c = (x == 0) ? TL : (x == p.getWidth() - 1 ? TR : HL);
        else if (y == p.getHeight() - 1) c = (x == 0) ? BL : (x == p.getWidth() - 1 ? BR : HL);
        else {
            if (x == 0 || x == p.getWidth() - 1) c = VL;
            else if (x > 1 && x < p.getWidth() - 2) {
                int textRow = y - 1;
                List<String> lines = p.getLines();
                if (textRow < lines.size()) {
                    String line = lines.get(textRow);
                    int textCol = x - 2;
                    if (textCol < line.length()) c = line.charAt(textCol);
                }
            }
        }
        return c;
    }

    private void drawBoxToBuffer(char[][] buffer, Post p, int screenW, int screenH) {
        int screenX = p.getX() - camX.get();
        int screenY = p.getY() - camY.get();

        for (int y = 0; y < p.getHeight(); y++) {
            int drawY = screenY + y;
            if (drawY < 0 || drawY >= screenH) continue;

            for (int x = 0; x < p.getWidth(); x++) {
                int drawX = screenX + x;
                if (drawX < 0 || drawX >= screenW) continue;

                char c = getCharacterAt(p, y, x);
                buffer[drawY][drawX] = c;
            }
        }
    }

    private void renderPosts() {
        for (Post p : posts) {
            if (p.getX() + p.getWidth() < camX.get()
                    || p.getX() > camX.get() + width
                    || p.getY() + p.getHeight() < camY.get()
                    || p.getY() > camY.get() + height) {
                continue;
            }
            p.update(deltaTime);
            drawBoxToBuffer(buffer, p, width, height);
        }
    }

    private void updateDisplay() {
        List<AttributedString> lines = new ArrayList<>(height);
        for (int r = 0; r < height; r++) {
            lines.add(new AttributedString(CharBuffer.wrap(buffer[r])));
        }
        display.update(lines, 0);
    }

    private void render() {
        handleResize();
        clearBuffer();
        renderPosts();
        updateDisplay();
    }

    public void startSync() throws IOException, InterruptedException {
        terminal.puts(InfoCmp.Capability.cursor_invisible);

        while (running.get()) {
            long startTime = System.currentTimeMillis();
            render();
            long usedTime = System.currentTimeMillis() - startTime;
            long sleepTime = Math.max((1000 / TARGET_FPS) - usedTime, 0);
            deltaTime = Math.max(1.0 / TARGET_FPS, usedTime / 1000.0);
            //noinspection BusyWait
            Thread.sleep(sleepTime);
        }

        terminal.puts(InfoCmp.Capability.clear_screen);
        terminal.puts(InfoCmp.Capability.cursor_visible);
        terminal.close();
    }
}
