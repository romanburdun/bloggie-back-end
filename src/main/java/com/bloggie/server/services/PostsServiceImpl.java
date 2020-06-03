package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.PostMapper;
import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.domain.Post;
import com.bloggie.server.repositories.PostsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;
    private final PostMapper postMapper;

    @Override
    public PostDTO createPost(PostDTO postDTO) {


        String slug = postDTO.getSlug().toLowerCase().replaceAll(" ", "-");
        postDTO.setSlug(slug);

        Post createdPost = postsRepository.save(postMapper.postDtoToPost(postDTO));


        return postMapper.postToPostDto(createdPost);
    }

    @Override
    public List<PostDTO> getPosts() {
        return postsRepository.findAll()
                                .stream()
                                .map(post -> postMapper.postToPostDto(post))
                                .collect(Collectors.toList());
    }

    @Override
    public PostDTO getPostBySlug(String slug) {
        return postMapper.postToPostDto(postsRepository.findBySlug(slug).get());
    }

    @Override
    public PostDTO deletePostBySlug(String slug) {

        Optional<Post> post =  postsRepository.findBySlug(slug);

        PostDTO postDTO = null;
        if(post.isPresent()) {
            postDTO = postMapper.postToPostDto(post.get());
            postsRepository.deleteById(post.get().getId());
        } else {
            System.out.println("Throwing 404 error");
        }

        return postDTO;
    }

    @Override
    public PostDTO updatePostBySlug( String slug, PostUpdateDTO update) {

        Optional<Post> foundPost = postsRepository.findBySlug(slug);

        if(!foundPost.isPresent()) {
            return null;
        }

        Post updatePost = foundPost.get();


        if(update.getPostTitle() != "" && update.getPostTitle() != null
                && !updatePost.getPostTitle().equals(update.getPostTitle())) {
            updatePost.setPostTitle(update.getPostTitle());
        }

        if(update.getContent() != null && !updatePost.getContent().equals(update.getContent())) {
            updatePost.setContent(update.getContent());
        }
        if(update.getSlug() != "" && update.getSlug() != null && !updatePost.getSlug().equals(update.getSlug())) {

            String updatedSlug = update.getSlug().toLowerCase().replaceAll(" ", "-");

            updatePost.setSlug(updatedSlug);
        }

        if(update.getPublicationDate() != null && !update.getPublicationDate().equals(updatePost.getPublicationDate())) {
                updatePost.setPublicationDate(update.getPublicationDate());
        }

        if(update.getCover() != null && !update.getCover().equals(updatePost.getCover())) {
            updatePost.setCover(update.getCover());
        }

        if(update.getReadTime() != 0 && update.getReadTime() != updatePost.getReadTime()) {
            updatePost.setReadTime(update.getReadTime());
        }

        return postMapper.postToPostDto(postsRepository.save(updatePost));
    }
}
