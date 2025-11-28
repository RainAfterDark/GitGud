package org.gitgud.data.posts;

import org.gitgud.core.model.StaticPost;

@SuppressWarnings("unused")
// Replace ExamplePost with the name of your .java file
public class ThisIsAPost extends StaticPost {
    // Replace ExamplePost here as well
    public ThisIsAPost() {
        // Enter your message inside the triple quotes
        super("""
            Post from workshop!!!
                """, true); // You can set wrap to false if you prefer
    }
}
