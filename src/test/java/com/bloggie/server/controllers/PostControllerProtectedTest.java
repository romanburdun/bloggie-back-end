package com.bloggie.server.controllers;

import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.fixtures.AuthTestConfig;
import com.bloggie.server.fixtures.PostsFixtures;
import com.bloggie.server.fixtures.UsersFixtures;
import com.bloggie.server.misc.PostsPaged;
import com.bloggie.server.services.PostsService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import javax.servlet.http.Cookie;
import java.util.Date;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = AuthTestConfig.class,webEnvironment = SpringBootTest.WebEnvironment.MOCK )
@ExtendWith(MockitoExtension.class)
class PostControllerProtectedTest extends AsJsonController {

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private PostsService postsService;

    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }


    @Test
    @WithUserDetails("jadmin@test.com")
    void createPostAuthenticatedTest() throws Exception {

        PostDTO post = new PostDTO();
        post.setTitle("Test post title");
        post.setContent("Test post content");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post");

        Mockito.when(postsService.createPost(any(PostDTO.class))).thenReturn(PostsFixtures.getSinglePostDTO());

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
    @WithUserDetails("jadmin@test.com")
    void createPostBadRequestTest() throws Exception {

        PostDTO post = new PostDTO();
        post.setTitle("Test post title");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post");

        Mockito.when(postsService.createPost(any(PostDTO.class))).thenThrow(new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(post)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiRequestException))
                .andExpect(result -> assertEquals("Bad request", result.getResolvedException().getMessage()))
                .andReturn()
                .getResponse()
                .getContentAsString();


    }


    @Test
    @WithAnonymousUser
    void createPostNotAuthenticatedTest() throws Exception {

        PostDTO post = new PostDTO();
        post.setTitle("Test post title");
        post.setContent("Test post content");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post");

        Mockito.when(postsService.createPost(any(PostDTO.class))).thenReturn(PostsFixtures.getSinglePostDTO());

        String data = asJsonString(post);
        System.out.println(data);
        mockMvc.perform(post("/api/v1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(data))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();


    }

    @Test
    @WithUserDetails("jadmin@test.com")
    void getPosts() throws Exception {

        PostsPaged response = new PostsPaged(1,PostsFixtures.getPostsDTOs());
        Mockito.when(postsService.getPosts(0, 3)).thenReturn(response);

        mockMvc.perform(get("/api/v1/posts?page=1&posts=3"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithUserDetails("jadmin@test.com")
    void deletePostAuthenticated() throws Exception {



        Mockito.when(postsService.deletePostBySlug(any(String.class))).thenReturn(PostsFixtures.getSinglePostDTO());
        int MS_IN_MINUTE = 60000;

        String token = Jwts.builder()
                .setSubject(Long.toString(UsersFixtures.getWriterUser().getId()))
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 15 * MS_IN_MINUTE))
                .signWith(SignatureAlgorithm.HS512, "SECRET")
                .compact();


        mockMvc.perform(delete("/api/v1/posts/test-post").cookie(new Cookie("token", token)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithAnonymousUser
    void deletePostNotAuthenticated() throws Exception {
        Mockito.when(postsService.deletePostBySlug(any(String.class))).thenReturn(PostsFixtures.getSinglePostDTO());

        mockMvc.perform(delete("/api/v1/posts/test-post"))
                .andExpect(status().isUnauthorized())
                .andReturn().getResponse().getContentAsString();
    }

    @Test
    @WithUserDetails("jwriter@test.com")
    void deletePostForbidden() throws Exception {
        Mockito.when(postsService.deletePostBySlug(any(String.class))).thenThrow(new ApiRequestException("Not authorized", HttpStatus.FORBIDDEN));

        mockMvc.perform(delete("/api/v1/posts/no-post"))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiRequestException))
                .andExpect(result -> assertEquals("Not authorized",result.getResolvedException().getMessage()))
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    @WithUserDetails("jwriter@test.com")
    void deletePostNotFound() throws Exception {
     Mockito.when(postsService.deletePostBySlug(any(String.class))).thenThrow(new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(delete("/api/v1/posts/no-post"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiRequestException))
                .andExpect(result-> assertEquals("Post not found",result.getResolvedException().getMessage()))
                .andReturn().getResponse().getContentAsString();
    }


    @Test
    @WithUserDetails("jadmin@test.com")
    void updatePostAuthenticated() throws Exception {
        PostUpdateDTO update = new PostUpdateDTO();
        update.setTitle("Test post title updated");
        update.setContent("Test post content updated");
        update.setCover("updatedTestCover.webp");
        update.setSlug("updated-test-post");
        update.setReadTime(5);

        Mockito.when(postsService.updatePostBySlug(any(String.class),any(PostUpdateDTO.class))).thenReturn(PostsFixtures.getUpdatedPostDTO());

         mockMvc.perform(put("/api/v1/posts/test-post").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(update)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

    }

    @Test
    @WithUserDetails("jadmin@test.com")
    void updatePostUnauthorized() throws Exception {
        PostUpdateDTO update = new PostUpdateDTO();
        update.setTitle("Test post title updated");
        update.setContent("Test post content updated");
        update.setCover("updatedTestCover.webp");
        update.setSlug("updated-test-post");
        update.setReadTime(5);

        Mockito.when(postsService.updatePostBySlug(any(String.class),any(PostUpdateDTO.class))).thenThrow(new ApiRequestException("Unauthorized request", HttpStatus.FORBIDDEN));

        mockMvc.perform(put("/api/v1/posts/test-post").contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(update)))
                .andExpect(status().isForbidden())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiRequestException))
                .andExpect(result -> assertEquals("Unauthorized request",result.getResolvedException().getMessage()))
                .andReturn().getResponse().getContentAsString();

    }



}
