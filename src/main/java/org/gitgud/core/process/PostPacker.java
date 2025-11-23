package org.gitgud.core.process;

import org.gitgud.core.model.Post;

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
            int newX = point[0] - (p.getWidth() / 2);
            int newY = point[1] - (p.getHeight() / 2);

            boolean collides = false;
            for (Post p2 : placed) {
                if (newX < p2.getX() + p2.getWidth() + PAD &&
                        newX + p.getWidth() + PAD > p2.getX() &&
                        newY < p2.getY() + p2.getHeight() + PAD &&
                        newY + p.getHeight() + PAD > p2.getY()) {
                    collides = true;
                    break;
                }
            }

            if (!collides) {
                p.setX(newX);
                p.setY(newY);
                placed.add(p);
                break;
            }
        }
    }
}
