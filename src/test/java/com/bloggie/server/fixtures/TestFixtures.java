package com.bloggie.server.fixtures;

import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.domain.Post;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public abstract class TestFixtures {

    public static List<Post> getPosts() {

        Post postOne = new Post();
        postOne.setId(1L);
        postOne.setDateCreated( LocalDateTime.now());
        postOne.setDateUpdated( LocalDateTime.now());
        postOne.setPostTitle("Test post one title");
        postOne.setContent("Test post one content");
        postOne.setCover("testPostOne.webp");
        postOne.setReadTime(10);
        postOne.setSlug("test-post-one");
        postOne.setPublicationDate(LocalDateTime.now());


        Post postTwo = new Post();
        postTwo.setId(2L);
        postTwo.setDateCreated( LocalDateTime.now());
        postTwo.setDateUpdated( LocalDateTime.now());
        postTwo.setPostTitle("Test post two title");
        postTwo.setContent("Test post two content");
        postTwo.setCover("testPostTwo.webp");
        postTwo.setReadTime(25);
        postTwo.setSlug("test-post-two");
        postTwo.setPublicationDate(LocalDateTime.now());

        Post postThree = new Post();
        postThree.setId(3L);
        postThree.setDateCreated( LocalDateTime.now());
        postThree.setDateUpdated( LocalDateTime.now());
        postThree.setPostTitle("Test post three title");
        postThree.setContent("Test post three content");
        postThree.setCover("testPostThree.webp");
        postThree.setReadTime(5);
        postThree.setSlug("post-three");

        return Arrays.asList(postOne, postTwo, postThree);
    }

    public static List<PostDTO> getPostsDTOs() {

        PostDTO postOne = new PostDTO();
        postOne.setId(1L);
        postOne.setDateCreated( LocalDateTime.now());
        postOne.setDateUpdated( LocalDateTime.now());
        postOne.setPostTitle("Test post one title");
        postOne.setContent("Test post one content");
        postOne.setCover("testPostOne.webp");
        postOne.setReadTime(10);
        postOne.setSlug("test-post-one");
        postOne.setPublicationDate(LocalDateTime.now());


        PostDTO postTwo = new PostDTO();
        postTwo.setId(2L);
        postTwo.setDateCreated( LocalDateTime.now());
        postTwo.setDateUpdated( LocalDateTime.now());
        postTwo.setPostTitle("Test post two title");
        postTwo.setContent("Test post two content");
        postTwo.setCover("testPostTwo.webp");
        postTwo.setReadTime(25);
        postTwo.setSlug("test-post-two");
        postTwo.setPublicationDate(LocalDateTime.now());

        PostDTO postThree = new PostDTO();
        postThree.setId(3L);
        postThree.setDateCreated( LocalDateTime.now());
        postThree.setDateUpdated( LocalDateTime.now());
        postThree.setPostTitle("Test post three title");
        postThree.setContent("Test post three content");
        postThree.setCover("testPostThree.webp");
        postThree.setReadTime(5);
        postThree.setSlug("post-three");

        return Arrays.asList(postOne, postTwo, postThree);
    }

    public static Post getSinglePost() {
        Post post = new Post();
        post.setId(1L);
        post.setDateCreated( LocalDateTime.now());
        post.setDateUpdated( LocalDateTime.now());
        post.setPostTitle("Test post title");
        post.setContent("Test post content");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post");
        post.setPublicationDate(LocalDateTime.now());

        return post;
    }

    public static PostDTO getSinglePostDTO() {
        PostDTO post = new PostDTO();
        post.setId(1L);
        post.setDateCreated( LocalDateTime.now());
        post.setDateUpdated( LocalDateTime.now());
        post.setPostTitle("Test post title");
        post.setContent("Test post content");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post");
        post.setPublicationDate(LocalDateTime.now());

        return post;
    }

    public static Post getUpdatedPost() {
        Post post = new Post();
        post.setId(1L);
        post.setDateCreated( LocalDateTime.now());
        post.setDateUpdated( LocalDateTime.now());
        post.setPostTitle("Test post title updated");
        post.setContent("Test post content updated");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post-updated");
        post.setPublicationDate(LocalDateTime.now());

        return post;
    }

    public static PostDTO getUpdatedPostDTO() {
        PostDTO post = new PostDTO();
        post.setId(1L);
        post.setDateCreated( LocalDateTime.now());
        post.setDateUpdated( LocalDateTime.now());
        post.setPostTitle("Test post title updated");
        post.setContent("Test post content updated");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post-updated");
        post.setPublicationDate(LocalDateTime.now());

        return post;
    }
}
