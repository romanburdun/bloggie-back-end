package com.bloggie.server.fixtures;

import com.bloggie.server.api.v1.models.PageDTO;
import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostExcerptDTO;
import com.bloggie.server.domain.*;
import org.assertj.core.util.Sets;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public abstract class TestFixtures {

    public static List<Post> getPosts() {

        Post postOne = new Post();
        postOne.setId(1L);
        postOne.setDateCreated( LocalDateTime.now());
        postOne.setDateUpdated( LocalDateTime.now());
        postOne.setTitle("Test post one title");
        postOne.setContent("Test post one content");
        postOne.setCover("testPostOne.webp");
        postOne.setReadTime(10);
        postOne.setSlug("test-post-one");
        postOne.setDatePublished(LocalDateTime.now());
        postOne.setAuthor(getUser());


        Post postTwo = new Post();
        postTwo.setId(2L);
        postTwo.setDateCreated( LocalDateTime.now());
        postTwo.setDateUpdated( LocalDateTime.now());
        postTwo.setTitle("Test post two title");
        postTwo.setContent("Test post two content");
        postTwo.setCover("testPostTwo.webp");
        postTwo.setReadTime(25);
        postTwo.setSlug("test-post-two");
        postTwo.setDatePublished(LocalDateTime.now());
        postTwo.setAuthor(getUser());

        Post postThree = new Post();
        postThree.setId(3L);
        postThree.setDateCreated( LocalDateTime.now());
        postThree.setDateUpdated( LocalDateTime.now());
        postThree.setTitle("Test post three title");
        postThree.setContent("Test post three content");
        postThree.setCover("testPostThree.webp");
        postThree.setReadTime(5);
        postThree.setSlug("post-three");
        postThree.setAuthor(getUser());

        return Arrays.asList(postOne, postTwo, postThree);
    }

    public static List<PostDTO> getPostsDTOs() {

        PostDTO postOne = new PostDTO();
        postOne.setDateCreated( LocalDateTime.now());
        postOne.setDateUpdated( LocalDateTime.now());
        postOne.setTitle("Test post one title");
        postOne.setContent("Test post one content");
        postOne.setCover("testPostOne.webp");
        postOne.setReadTime(10);
        postOne.setSlug("test-post-one");
        postOne.setDatePublished(LocalDateTime.now());


        PostDTO postTwo = new PostDTO();
        postTwo.setDateCreated( LocalDateTime.now());
        postTwo.setDateUpdated( LocalDateTime.now());
        postTwo.setTitle("Test post two title");
        postTwo.setContent("Test post two content");
        postTwo.setCover("testPostTwo.webp");
        postTwo.setReadTime(25);
        postTwo.setSlug("test-post-two");
        postTwo.setDatePublished(LocalDateTime.now());

        PostDTO postThree = new PostDTO();
        postThree.setDateCreated( LocalDateTime.now());
        postThree.setDateUpdated( LocalDateTime.now());
        postThree.setTitle("Test post three title");
        postThree.setContent("Test post three content");
        postThree.setCover("testPostThree.webp");
        postThree.setReadTime(5);
        postThree.setSlug("post-three");

        return Arrays.asList(postOne, postTwo, postThree);
    }


    public static List<PostExcerptDTO> getPostsExcerptsDTOs() {

        PostExcerptDTO postTwoExcerpt = new PostExcerptDTO();
        postTwoExcerpt.setDateCreated( LocalDateTime.now());
        postTwoExcerpt.setDateUpdated( LocalDateTime.now());
        postTwoExcerpt.setTitle("Test post one title");
        postTwoExcerpt.setReadTime(10);
        postTwoExcerpt.setSlug("test-post-one");
        postTwoExcerpt.setDatePublished(LocalDateTime.now());


        PostExcerptDTO postTwoExcerptDTO = new PostExcerptDTO();
        postTwoExcerptDTO.setDateCreated( LocalDateTime.now());
        postTwoExcerptDTO.setDateUpdated( LocalDateTime.now());
        postTwoExcerptDTO.setTitle("Test post two title");
        postTwoExcerptDTO.setReadTime(25);
        postTwoExcerptDTO.setSlug("test-post-two");
        postTwoExcerptDTO.setDatePublished(LocalDateTime.now());

        PostExcerptDTO postThreeExcerpt = new PostExcerptDTO();
        postThreeExcerpt.setDateCreated( LocalDateTime.now());
        postThreeExcerpt.setDateUpdated( LocalDateTime.now());
        postThreeExcerpt.setTitle("Test post three title");
        postThreeExcerpt.setReadTime(5);
        postThreeExcerpt.setSlug("post-three");

        return Arrays.asList(postTwoExcerpt, postTwoExcerptDTO, postThreeExcerpt);
    }
    public static Post getSinglePost() {
        Post post = new Post();
        post.setId(1L);
        post.setDateCreated( LocalDateTime.now());
        post.setDateUpdated( LocalDateTime.now());
        post.setTitle("Test post title");
        post.setContent("Test post content");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post");
        post.setDatePublished(LocalDateTime.now());
        post.setAuthor(getUser());

        return post;
    }

    public static PostDTO getSinglePostDTO() {
        PostDTO post = new PostDTO();
        post.setDateCreated( LocalDateTime.now());
        post.setDateUpdated( LocalDateTime.now());
        post.setTitle("Test post title");
        post.setContent("Test post content");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post");
        post.setDatePublished(LocalDateTime.now());

        return post;
    }

    public static Post getUpdatedPost() {
        Post post = new Post();
        post.setId(1L);
        post.setDateCreated( LocalDateTime.now());
        post.setDateUpdated( LocalDateTime.now());
        post.setTitle("Test post title updated");
        post.setContent("Test post content updated");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post-updated");
        post.setDatePublished(LocalDateTime.now());

        return post;
    }

    public static PostDTO getUpdatedPostDTO() {
        PostDTO post = new PostDTO();
        post.setDateCreated( LocalDateTime.now());
        post.setDateUpdated( LocalDateTime.now());
        post.setTitle("Test post title updated");
        post.setContent("Test post content updated");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post-updated");
        post.setDatePublished(LocalDateTime.now());

        return post;
    }

    public static Page getSinglePage() {
        Page page = new Page();
        page.setTitle("Test page");
        page.setContent("<div>Test page content</div>");
        page.setSlug("test-page");

        return page;
    }

    public static PageDTO getSinglePageDTO() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTitle("Test pageDTO");
        pageDTO.setContent("<div>Test pageDTO content</div>");
        pageDTO.setSlug("test-pageDTO");

        return pageDTO;
    }

    public static Page getUpdatedPage() {
        Page page = new Page();
        page.setTitle("Test page updated");
        page.setContent("<div>Test page content updated</div>");
        page.setSlug("test-page");

        return page;
    }

    public static PageDTO getUpdatedPageDTO() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTitle("Test pageDTO updated");
        pageDTO.setContent("<div>Test pageDTO content updated</div>");
        pageDTO.setSlug("test-pageDTO");

        return pageDTO;
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

    public static List<PageDTO> getPagesDTOs() {
        PageDTO blogPage = new PageDTO();
        blogPage.setTitle("Blog page");
        blogPage.setContent("<div>Blog page content here...</div>");
        blogPage.setSlug("blog");

        PageDTO aboutPage = new PageDTO();
        aboutPage.setTitle("About page");
        aboutPage.setContent("<div>About page content here...</div>");
        aboutPage.setSlug("about");
        return Arrays.asList(blogPage, aboutPage);
    }

    public static User getUser() {
        Role roleOne = new Role();
        roleOne.setName(RoleName.ROLE_WRITER);
        Role roleTwo = new Role();
        roleTwo.setName(RoleName.ROLE_ADMINISTRATOR);
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("jdoe@test.com");
        user.setPassword("secret");
        user.setRoles(new HashSet<>(Arrays.asList(roleOne, roleTwo)));

        return user;
    }

    public static Meta getPageMeta() {
        Meta pageMeta = new Meta();
        pageMeta.setSeoTitle("Test page SEO title");
        pageMeta.setSeoDescription("Test page SEO description");

        return pageMeta;
    }

    public static Meta getPostMeta() {
        Meta postMeta = new Meta();
        postMeta.setSeoTitle("Test post SEO title");
        postMeta.setSeoDescription("Test post SEO description");

        return postMeta;
    }
}
