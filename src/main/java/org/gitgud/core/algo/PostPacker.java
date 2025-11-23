package org.gitgud.core.algo;

import org.gitgud.core.data.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PostPacker implements Consumer<Post> {
    static final int PAD = 1;

    final List<Post> placed = new ArrayList<>();

    @Override
    public void accept(Post p) {
        SpiralIterator spiral = new SpiralIterator();
        while (true) {
            int[] point = spiral.next();
            int newX = point[0] - (p.width / 2);
            int newY = point[1] - (p.height / 2);

            boolean collides = false;
            for (Post p2 : placed) {
                if (newX < p2.x + p2.width + PAD &&
                        newX + p.width + PAD > p2.x &&
                        newY < p2.y + p2.height + PAD &&
                        newY + p.height + PAD > p2.y) {
                    collides = true;
                    break;
                }
            }

            if (!collides) {
                p.x = newX;
                p.y = newY;
                placed.add(p);
                break;
            }
        }
    }
}
