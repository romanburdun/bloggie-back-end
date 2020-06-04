package com.bloggie.server.controllers;

import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.fixtures.TestFixtures;
import com.bloggie.server.services.PostsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostControllerTest extends AsJsonController {

    @Mock
    private PostsService postsService;

    @InjectMocks
    private PostController postController;

    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(postController).build();
    }

    @Test
    void createPostTest() throws Exception {

        PostDTO post = new PostDTO();
        post.setTitle("Test post title");
        post.setContent("Test post content");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post");

        Mockito.when(postsService.createPost(any(PostDTO.class))).thenReturn(TestFixtures.getSinglePostDTO());

        String data = asJsonString(post);
        System.out.println(data);
        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


    }

    @Test
    void getPosts() throws Exception {
        Mockito.when(postsService.getPosts()).thenReturn(TestFixtures.getPostsDTOs());

        mockMvc.perform(get("/api/v1/posts"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void deletePost() throws Exception {
        Mockito.when(postsService.deletePostBySlug(any(String.class))).thenReturn(TestFixtures.getSinglePostDTO());

        mockMvc.perform(delete("/api/v1/posts/test-post"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    void updatePost() throws Exception {
        PostUpdateDTO update = new PostUpdateDTO();
        update.setTitle("Test post title updated");
        update.setContent("Test post content updated");
        update.setCover("updatedTestCover.webp");
        update.setSlug("updated-test-post");
        update.setReadTime(5);

        Mockito.when(postsService.updatePostBySlug("test-post",update)).thenReturn(TestFixtures.getUpdatedPostDTO());

         mockMvc.perform(put("/api/v1/posts/test-post").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(update)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    void getPostBySluTest() throws Exception {
        Mockito.when(postsService.getPostBySlug("test-post")).thenReturn(TestFixtures.getSinglePostDTO());

        mockMvc.perform(get("/api/v1/posts/test-post"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

    }
}
