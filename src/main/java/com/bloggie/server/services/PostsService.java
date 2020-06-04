package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostExcerptDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;



import java.util.List;

public interface PostsService {
    PostDTO createPost(PostDTO postDTO);
    List<PostDTO> getPosts();
    PostDTO getPostBySlug(String slug);
    PostDTO deletePostBySlug(String slug);
    PostDTO updatePostBySlug(String slug, PostUpdateDTO update);
    List<PostExcerptDTO> getPostsExcerpts();
}
