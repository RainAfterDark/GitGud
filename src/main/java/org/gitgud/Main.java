package org.gitgud;

import org.gitgud.core.algo.PostPacker;
import org.gitgud.core.data.Post;
import org.gitgud.core.terminal.ConsoleRenderer;
import org.gitgud.core.terminal.InputHandler;

import java.io.IOException;
import java.util.*;

public class Main {
    static void main() throws IOException, InterruptedException {
        List<String> dummyPosts = Arrays.asList(
                "Hello World", "Display Class", "Optimized Rendering",
                "WASD to move", "Grouped Updates", "Java + JLine",
                "The quick brown fox jumps over the lazy dog.",
                "Box 1", "Box 2", "Box 3", "Box 4", "Box 5",
                "Center", "Optimization is key",
                "This text box is intentionally long to test the wrapping logic."
        );

        List<Post> posts = new ArrayList<>();
        for (String msg : dummyPosts) posts.add(new Post(msg));
        for(int i=0; i<12; i++) {
            for(String msg : dummyPosts) posts.add(new Post(msg + " [" + i + "]"));
        }
        posts.sort((p1, p2) -> (p2.width * p2.height) - (p1.width * p1.height));
        posts.forEach(new PostPacker());

        ConsoleRenderer renderer = new ConsoleRenderer(posts);
        InputHandler inputHandler = new InputHandler(renderer);
        inputHandler.startThreaded();
        renderer.startBlocking();
        System.exit(0);
    }
}