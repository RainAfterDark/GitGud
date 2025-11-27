package org.gitgud.core.model;

import java.util.List;

/**
 * A factory for generating posts.
 */
public interface PostFactory {
    /**
     * Generate a list of posts.
     * @return a list of posts.
     */
    List<Post> generatePosts();
}
