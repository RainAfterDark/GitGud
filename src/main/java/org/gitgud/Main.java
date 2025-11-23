package org.gitgud;

import org.gitgud.core.process.PostCollector;
import org.gitgud.core.terminal.ConsoleRenderer;
import org.gitgud.core.terminal.InputHandler;

import java.io.IOException;

public class Main {
    static void main() throws IOException, InterruptedException {
        var posts = new PostCollector().collect();
        var renderer = new ConsoleRenderer(posts);
        var inputHandler = new InputHandler(renderer);
        inputHandler.startAsync();
        renderer.startSync();
        System.exit(0);
    }
}