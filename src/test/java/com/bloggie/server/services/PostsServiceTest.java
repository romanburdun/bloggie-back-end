package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.MediaMapper;
import com.bloggie.server.api.v1.mappers.MetaMapper;
import com.bloggie.server.api.v1.mappers.PostMapper;
import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostExcerptDTO;
import com.bloggie.server.api.v1.models.PostReaderDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.domain.Meta;
import com.bloggie.server.domain.Post;
import com.bloggie.server.domain.User;
import com.bloggie.server.fixtures.MetaFixtures;
import com.bloggie.server.fixtures.PostsFixtures;
import com.bloggie.server.fixtures.UsersFixtures;
import com.bloggie.server.misc.PostsExcerptsPaged;
import com.bloggie.server.misc.PostsPaged;
import com.bloggie.server.repositories.MediaRepository;
import com.bloggie.server.repositories.MetasRepository;
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
import static org.mockito.ArgumentMatchers.anyString;

class PostsServiceTest {

    @Mock
    private PostsService postsService;
    @Mock
    private PostsRepository postsRepository;
    @Mock
    private MetasRepository metasRepository;
    @Mock
    private MediaRepository mediaRepository;
    @Mock
    private AuthService authService;

    private PostMapper postMapper = PostMapper.INSTANCE;
    private MetaMapper metaMapper = MetaMapper.INSTANCE;
    private MediaMapper mediaMapper = MediaMapper.INSTANCE;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        postsService = new PostsServiceImpl(
                postsRepository,
                postMapper,
                authService,
                metaMapper,
                metasRepository,
                mediaMapper,
                mediaRepository);
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

        Mockito.when(metasRepository.save(any(Meta.class))).thenReturn(MetaFixtures.getPostMeta());
        Mockito.when(postsRepository.save(any(Post.class))).thenReturn(PostsFixtures.getSinglePost());
        Mockito.when(authService.getRequestUser()).thenReturn(Optional.of(UsersFixtures.getWriterUser()));

        PostDTO createdPost = postsService.createPost(postMapper.postToPostDto(post));

        assertNotNull(createdPost);
        assertNotNull("test-post", createdPost.getSlug());

    }

    @Test
    void getPostsAsWriter() {

        Page<Post> pagedPosts = new PageImpl(PostsFixtures.getPosts());

        Mockito.when(authService.getRequestUser()).thenReturn(Optional.of(UsersFixtures.getWriterUser()));
        Mockito.when(postsRepository.findAllByAuthor(any(User.class),any(Pageable.class))).thenReturn(pagedPosts);


        PostsPaged posts = postsService.getPosts(1, 3);

        assertNotNull(posts);
        assertEquals(3, posts.getPosts().size());

    }

    @Test
    void getPostsAsAdministrator() {

        Page<Post> pagedPosts = new PageImpl(PostsFixtures.getPosts());

        Mockito.when(authService.getRequestUser()).thenReturn(Optional.of(UsersFixtures.getAdminUser()));
        Mockito.when(postsRepository.findAll(any(Pageable.class))).thenReturn(pagedPosts);

        PostsPaged posts = postsService.getPosts(1, 3);

        assertNotNull(posts);
        assertEquals(3, posts.getPosts().size());

    }


    @Test
    void deletePostBySlug() {

        Mockito.when(authService.getRequestUser()).thenReturn(Optional.of(UsersFixtures.getWriterUser()));
        Mockito.when(postsRepository.findBySlug(any(String.class))).thenReturn(Optional.of(PostsFixtures.getSinglePost()));
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

        Mockito.when(authService.getRequestUser()).thenReturn(Optional.of(UsersFixtures.getWriterUser()));
        Mockito.when(postsRepository.findBySlug(any(String.class))).thenReturn(Optional.of(PostsFixtures.getSinglePost()));
        Mockito.when(postsRepository.save(any(Post.class))).thenReturn(PostsFixtures.getUpdatedPost());

        PostDTO updatedPost = postsService.updatePostBySlug("test-post", update);

        assertNotNull(updatedPost);
        assertEquals("test-post-updated", updatedPost.getSlug());



    }

    @Test
    void getPostBySlug() {

        Mockito.when(postsRepository.findBySlugAndDraftIsFalse(any(String.class))).thenReturn(Optional.of(PostsFixtures.getSinglePost()));
        PostReaderDTO fetchedPost = postsService.getPostBySlug("test-post");

        assertNotNull(fetchedPost);
        assertEquals("test-post", fetchedPost.getSlug());
    }

    @Test
    void getPostsExcerpts() {

        Page<Post> pagedPosts = new PageImpl(PostsFixtures.getPosts());

        Mockito.when(postsRepository.findAllByDatePublishedBeforeAndDraftIsFalse(any(LocalDateTime.class),any(Pageable.class))).thenReturn(pagedPosts);


        PostsExcerptsPaged fetchedExcerpts = postsService.getPostsExcerpts(0, 3);

        assertNotNull(fetchedExcerpts);
        assertEquals(3, fetchedExcerpts.getPostsExcerpts().size());
    }

    @Test
    void searchPostsByTitle() {

        Mockito.when(postsRepository.findAllByTitleIgnoreCaseContaining(anyString())).thenReturn(PostsFixtures.getPosts());

        List<PostExcerptDTO> foundPosts = postsService.searchPostsByTitle("test");

        assertNotNull(foundPosts);
        assertEquals(3, foundPosts.size());
    }

    @Test
    void searchPostsByContent() {

        Mockito.when(postsRepository.findAllByContentIgnoreCaseContaining(anyString())).thenReturn(PostsFixtures.getPosts());

        List<PostExcerptDTO> foundPosts = postsService.searchPostsByContent("test");

        assertNotNull(foundPosts);
        assertEquals(3, foundPosts.size());
    }
}
