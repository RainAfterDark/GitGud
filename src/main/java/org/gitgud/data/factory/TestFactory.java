package org.gitgud.data.factory;

import org.gitgud.core.model.Post;
import org.gitgud.core.model.PostFactory;
import org.gitgud.core.model.TextPost;

import java.util.List;

@SuppressWarnings("unused")
public class TestFactory implements PostFactory {
    @Override
    public List<Post> generatePosts() {
        return List.of(
                new TextPost("Hello from Factory!", true),
                new TextPost("Hi from Factory!", false)
        );
    }
}
