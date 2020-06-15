package com.bloggie.server.bootstrap;

import com.bloggie.server.domain.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public abstract class BootstrapData {

    public static List<Post> getPosts() {

        Post postOne = new Post();
        postOne.setId(1L);
        postOne.setDateCreated( LocalDateTime.now());
        postOne.setDateUpdated( LocalDateTime.now());
        postOne.setTitle("Post one title");
        postOne.setContent("Post one content");
        postOne.setExcerpt("Post one excerpt");
        postOne.setCover("postOne.webp");
        postOne.setReadTime(10);
        postOne.setSlug("post-one");
        postOne.setDatePublished(LocalDateTime.now());


        Post postTwo = new Post();
        postTwo.setId(2L);
        postTwo.setDateCreated( LocalDateTime.now());
        postTwo.setDateUpdated( LocalDateTime.now());
        postTwo.setTitle("Post two title");
        postTwo.setContent("Post two content");
        postTwo.setExcerpt("Post two excerpt");
        postTwo.setCover("postTwo.webp");
        postTwo.setReadTime(25);
        postTwo.setSlug("post-two");
        postTwo.setDatePublished(LocalDateTime.now());

        Post postThree = new Post();
        postThree.setId(3L);
        postThree.setDateCreated( LocalDateTime.now());
        postThree.setDateUpdated( LocalDateTime.now());
        postThree.setTitle("Post three title");
        postThree.setContent("Post three content");
        postThree.setExcerpt("Post three excerpt");
        postThree.setCover("postThree.webp");
        postThree.setReadTime(5);
        postThree.setSlug("post-three");
        postThree.setDatePublished(LocalDateTime.now());

        return Arrays.asList(postOne, postTwo, postThree);
    }

    public static List<Page> getPages() {
        Page blogPage = new Page();
        blogPage.setTitle("Blog page");
        blogPage.setContent("<div>Blog page content here...</div>");
        blogPage.setSlug("blog");

        Page aboutPage = new Page();
        aboutPage.setTitle("About page");
        aboutPage.setContent("<div>About page content here...</div>");
        aboutPage.setSlug("about");

        return Arrays.asList(blogPage, aboutPage);

    }

    public static List<Role> getRoles() {
        Role roleOne = new Role();
        roleOne.setName(RoleName.ROLE_ADMINISTRATOR);

        Role roleTwo = new Role();
        roleTwo.setName(RoleName.ROLE_WRITER);
        return Arrays.asList(roleOne, roleTwo);
    }

    public static SiteSettings getSettings () {
        SiteSettings settings = new SiteSettings();

        settings.setPostsPerPage(5);
        settings.setRegistrationAllowed(true);
        return settings;
    }
}
