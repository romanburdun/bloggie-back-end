package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.PostMapper;
import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostExcerptDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.domain.Post;
import com.bloggie.server.fixtures.TestFixtures;
import com.bloggie.server.misc.PostsExcerptsPaged;
import com.bloggie.server.misc.PostsPaged;
import com.bloggie.server.repositories.PostsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class PostsServiceTest {

    @Mock
    private PostsService postsService;
    @Mock
    private PostsRepository postsRepository;
    private PostMapper postMapper = PostMapper.INSTANCE;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        postsService = new PostsServiceImpl(postsRepository, postMapper);
    }


    @Test
    void createPost() {

        Post post = new Post();
        post.setTitle("Test post title");
        post.setContent("Test post content");
        post.setCover("testPost.webp");
        post.setReadTime(10);
        post.setSlug("test-post");
        post.setDatePublished(LocalDateTime.now());

        Mockito.when(postsRepository.save(any(Post.class))).thenReturn(TestFixtures.getSinglePost());

        PostDTO createdPost = postsService.createPost(postMapper.postToPostDto(post));

        assertNotNull(createdPost);
        assertNotNull("test-post", createdPost.getSlug());

    }

    @Test
    void getPosts() {

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "dateCreated");
        Page<Post> pagedPosts = new PageImpl(TestFixtures.getPosts());

        Mockito.when(postsRepository.findAll(pageRequest)).thenReturn(pagedPosts);

        PostsPaged posts = postsService.getPosts(1, 3);

        assertNotNull(posts);
        assertEquals(3, posts.getPosts().size());

    }

    @Test
    void deletePostBySlug() {

        Mockito.when(postsRepository.findBySlug(any(String.class))).thenReturn(Optional.of(TestFixtures.getSinglePost()));
        PostDTO deletedPost = postsService.deletePostBySlug("test-post");
        assertNotNull(deletedPost);
        assertEquals("test-post", deletedPost.getSlug());
    }

    @Test
    void updatePostBySlug() {

        PostUpdateDTO update = new PostUpdateDTO();
        update.setTitle("Test post title updated");
        update.setContent("Test post content updated");
        update.setSlug("test-post-updated");

        Mockito.when(postsRepository.findBySlug(any(String.class))).thenReturn(Optional.of(TestFixtures.getSinglePost()));
        Mockito.when(postsRepository.save(any(Post.class))).thenReturn(TestFixtures.getUpdatedPost());

        PostDTO updatedPost = postsService.updatePostBySlug("test-post", update);

        assertNotNull(updatedPost);
        assertEquals("test-post-updated", updatedPost.getSlug());



    }

    @Test
    void getPostBySlug() {

        Mockito.when(postsRepository.findBySlug(any(String.class))).thenReturn(Optional.of(TestFixtures.getSinglePost()));
        PostDTO fetchedPost = postsService.getPostBySlug("test-post");

        assertNotNull(fetchedPost);
        assertEquals("test-post", fetchedPost.getSlug());
    }

    @Test
    void getPostsExcerpts() {

        Pageable pageRequest = PageRequest.of(0, 3, Sort.Direction.DESC, "dateCreated");
        Page<Post> pagedPosts = new PageImpl(TestFixtures.getPosts());

        Mockito.when(postsRepository.findAllByDatePublishedBefore(any(LocalDateTime.class),any(Pageable.class))).thenReturn(pagedPosts);


        PostsExcerptsPaged fetchedExcerpts = postsService.getPostsExcerpts(0, 3);

        assertNotNull(fetchedExcerpts);
        assertEquals(3, fetchedExcerpts.getPostsExcerpts().size());
    }
}
