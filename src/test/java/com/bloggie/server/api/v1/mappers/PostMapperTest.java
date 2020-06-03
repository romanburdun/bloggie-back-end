package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PostMapperTest {


    private final PostMapper postMapper = PostMapper.INSTANCE;

    @Test
    void postDtoToPost() {

        PostDTO postDTO = new PostDTO();
        postDTO.setId(1L);
        postDTO.setDateCreated( LocalDateTime.now());
        postDTO.setDateUpdated( LocalDateTime.now());
        postDTO.setPostTitle("DTO test title");
        postDTO.setContent("DTO test content");
        postDTO.setCover("testDto.webp");
        postDTO.setReadTime(5);


        Post post = postMapper.postDtoToPost(postDTO);
        assertNotNull(post);
        assertEquals("DTO test content", post.getContent());
        assertEquals("testDto.webp", post.getCover());


    }

    @Test
    void postToPostDto() {

        Post post = new Post();
        post.setId(1L);
        post.setDateCreated( LocalDateTime.now());
        post.setDateUpdated( LocalDateTime.now());
        post.setPostTitle("Test title");
        post.setContent("Test content");
        post.setCover("test.webp");
        post.setReadTime(10);


        PostDTO postDTO = postMapper.postToPostDto(post);
        assertNotNull(postDTO);
        assertEquals("Test content", postDTO.getContent());
        assertEquals("test.webp", postDTO.getCover());

    }
}
