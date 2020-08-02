package com.bloggie.server.controllers;

import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.fixtures.PostsFixtures;
import com.bloggie.server.misc.PostsExcerptsPaged;
import com.bloggie.server.services.PostsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK )
@ExtendWith(MockitoExtension.class)
public class PostControllerPublicTest {

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private PostsService postsService;


    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @Test
    void getPostBySlugTest() throws Exception {
        Mockito.when(postsService.getPostBySlug("test-post")).thenReturn(PostsFixtures.getSinglePostReader());
        mockMvc.perform(get("/api/v1/posts/test-post"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    void getPostBySlugNotFoundTest() throws Exception {
        Mockito.when(postsService.getPostBySlug("test-post")).thenThrow(new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));
        mockMvc.perform(get("/api/v1/posts/test-post"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiRequestException))
                .andExpect(result -> assertEquals("Post not found",result.getResolvedException().getMessage()))
                .andReturn().getResponse().getContentAsString();

    }

    @Test

    void getPostsExcerpts() throws Exception {
        PostsExcerptsPaged response = new PostsExcerptsPaged(1,PostsFixtures.getPostsExcerptsDTOs());
        Mockito.when(postsService.getPostsExcerpts(0, 3)).thenReturn(response);

        mockMvc.perform(get("/api/v1/posts/excerpt?page=0&posts=3"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

    }

}
