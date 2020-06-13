package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostExcerptDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.misc.PostsExcerptsPaged;
import com.bloggie.server.misc.PostsPaged;
import org.springframework.core.io.Resource;

import java.util.List;


public interface PostsService {
    PostDTO createPost(PostDTO postDTO);
    PostsPaged getPosts(int page, int posts);
    PostDTO getPostBySlug(String slug);
    PostDTO deletePostBySlug(String slug);
    PostDTO updatePostBySlug(String slug, PostUpdateDTO update);
    PostsExcerptsPaged getPostsExcerpts(int page, int posts);
    Resource getPostCover(String fileName);
    List<PostExcerptDTO> searchPostsByTitle(String searchTitle);
    List<PostExcerptDTO> searchPostsByContent(String searchContent);


}
