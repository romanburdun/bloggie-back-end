package com.bloggie.server.controllers;

import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.services.PostsService;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostsService postsService;

    @PostMapping
    private PostDTO createPost(@RequestBody PostDTO postDTO) {
        return postsService.createPost(postDTO);
    }

    @GetMapping
    private List<PostDTO> getPosts() {
        return postsService.getPosts();
    }

    @GetMapping("/{slug}")
    private PostDTO getPostBySlug(@PathVariable String slug) {
        return postsService.getPostBySlug(slug);
    }

    @PutMapping("/{slug}")
    private PostDTO updatePost(@PathVariable String slug, @RequestBody PostUpdateDTO updateDTO) {
        return postsService.updatePostBySlug(slug, updateDTO);
    }


    @DeleteMapping("/{slug}")
    private PostDTO deletePost(@PathVariable String slug) {
        return postsService.deletePostBySlug(slug);
    }
}
