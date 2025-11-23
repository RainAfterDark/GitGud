package org.gitgud.core.process;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.gitgud.core.model.BasePost;
import org.gitgud.core.model.Post;
import org.gitgud.core.model.PostFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PostCollector {
    final List<Post> posts = new ArrayList<>();

    void collectFromClasses(ScanResult scanResult) {
        var postClasses = scanResult.getSubclasses(Post.class)
                .filter(c -> !c.hasAnnotation(BasePost.class))
                .loadClasses(Post.class);
        postClasses.forEach(clazz -> {
            try {
                var constructor = Arrays.stream(clazz.getDeclaredConstructors())
                        .filter(c -> c.getParameterCount() == 0)
                        .findFirst();
                if (constructor.isEmpty()) return;
                Post post = (Post) constructor.get().newInstance();
                posts.add(post);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    void collectFromFactories(ScanResult scanResult) {
        var factories = scanResult.getClassesImplementing(PostFactory.class)
                .loadClasses(PostFactory.class);
        factories.forEach(clazz -> {
            try {
                var constructor = Arrays.stream(clazz.getDeclaredConstructors())
                        .filter(c -> c.getParameterCount() == 0)
                        .findFirst();
                if (constructor.isEmpty()) return;
                PostFactory factory = (PostFactory) constructor.get().newInstance();
                posts.addAll(factory.generatePosts());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    void collectFromReflection() {
        try (ScanResult scanResult = new ClassGraph()
                .acceptPackages("org.gitgud")
                .enableAllInfo().scan()) {
            collectFromClasses(scanResult);
            collectFromFactories(scanResult);
        }
    }

    void processPosts() {
        posts.sort((p1, p2) ->
                (p2.getWidth() * p2.getHeight()) - (p1.getWidth() * p1.getHeight()));
        posts.forEach(new PostPacker());
    }

    public List<Post> collect() {
        collectFromReflection();
        processPosts();
        return posts;
    }
}
