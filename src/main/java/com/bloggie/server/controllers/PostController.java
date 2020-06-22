package com.bloggie.server.controllers;

import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostExcerptDTO;
import com.bloggie.server.api.v1.models.PostReaderDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.misc.PostsExcerptsPaged;
import com.bloggie.server.misc.PostsPaged;
import com.bloggie.server.services.PostsService;
import lombok.AllArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
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
    private PostsPaged getPosts(@RequestParam int page, @RequestParam int posts) {
        return postsService.getPosts(page, posts);
    }

    @GetMapping("/{slug}")
    private PostReaderDTO getPostBySlug(@PathVariable String slug) {
        return postsService.getPostBySlug(slug);
    }

    @PutMapping("/{slug}")
    private PostDTO updatePost(@PathVariable String slug, @RequestBody PostUpdateDTO updateDTO) {
        return postsService.updatePostBySlug(slug, updateDTO);
    }

    @GetMapping("/excerpts")
    private PostsExcerptsPaged getPostsExcerpts(@RequestParam int page, @RequestParam int posts) {
        return postsService.getPostsExcerpts(page, posts);
    }

    @GetMapping("/by-title")
    private List<PostExcerptDTO> searchPostsByTitle(@RequestParam String title) {
        return postsService.searchPostsByTitle(title);
    }

    @GetMapping("/by-content")
    private List<PostExcerptDTO> searchPostsByContent(@RequestParam String content) {
        return postsService.searchPostsByContent(content);
    }


    @DeleteMapping("/{slug}")
    private PostDTO deletePost(@PathVariable String slug) {
        return postsService.deletePostBySlug(slug);
    }
}
