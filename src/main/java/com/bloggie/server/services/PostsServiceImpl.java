package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.PostMapper;
import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostExcerptDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.domain.Post;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.misc.PostsExcerptsPaged;
import com.bloggie.server.misc.PostsPaged;
import com.bloggie.server.repositories.PostsRepository;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;
    private final PostMapper postMapper;

    private final String COVERS_PATH = "images/covers";

    @Override
    public PostDTO createPost(PostDTO postDTO) {


        if(postDTO.getTitle() == null || postDTO.getContent() == null ) {
            throw new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST);
        }

        if(postDTO.getTitle() == "" || postDTO.getContent() == "") {
            throw new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST);
        }

        if(postDTO.getDatePublished() == null) {
            postDTO.setDatePublished(LocalDateTime.now());
        }


        String slug;
        if (postDTO.getSlug() == null || postDTO.getSlug() == "") {

            slug = postDTO.getTitle().toLowerCase().replaceAll(" ", "-");
        } else {
            slug = postDTO.getSlug().toLowerCase().replaceAll(" ", "-");
        }

        postDTO.setSlug(slug);


        if(postDTO.getCover().startsWith("data:")) {
            postDTO.setCover(saveImage(postDTO.getCover(), slug, COVERS_PATH));
        } else {
            postDTO.setCover("");
        }



        Post createdPost = postsRepository.save(postMapper.postDtoToPost(postDTO));


        return postMapper.postToPostDto(createdPost);
    }

    @Override
    public PostsPaged getPosts(int page, int posts) {

        if(page > 0) {
            page--;
        }

        PageRequest pageRequest = PageRequest.of(page, posts, Sort.Direction.DESC, "dateCreated");

        List<PostDTO> postsList = postsRepository.findAll(pageRequest)
                .stream()
                .map(post -> postMapper.postToPostDto(post))
                .collect(Collectors.toList());

        return new PostsPaged(getTotalPages(posts), postsList);
    }

    @Override
    public PostDTO getPostBySlug(String slug) {

        Optional<Post> post = postsRepository.findBySlug(slug);

        if(!post.isPresent()) {
            throw new ApiRequestException("Post not found", HttpStatus.NOT_FOUND);
        }

        return postMapper.postToPostDto(post.get());
    }

    @Override
    public PostDTO deletePostBySlug(String slug) {

        Optional<Post> post =  postsRepository.findBySlug(slug);

        if(!post.isPresent()) {
            throw new ApiRequestException("Post not found", HttpStatus.NOT_FOUND);
        }

        File deleteCover = new File(COVERS_PATH + "/" + post.get().getCover());

        if(deleteCover.exists()) {
            deleteCover.delete();
        }
        postsRepository.deleteById(post.get().getId());

        return postMapper.postToPostDto(post.get());
    }

    @Override
    public PostDTO updatePostBySlug( String slug, PostUpdateDTO update) {

        Optional<Post> foundPost = postsRepository.findBySlug(slug);

        if(!foundPost.isPresent()) {
            throw new ApiRequestException("Post not found", HttpStatus.NOT_FOUND);
        }

        Post updatePost = foundPost.get();


        if(update.getTitle() != "" && update.getTitle() != null
                && !updatePost.getTitle().equals(update.getTitle())) {
            updatePost.setTitle(update.getTitle());
        }

        if(update.getContent() != null && !updatePost.getContent().equals(update.getContent())) {
            updatePost.setContent(update.getContent());
        }
        if(update.getSlug() != "" && update.getSlug() != null && !updatePost.getSlug().equals(update.getSlug())) {

            String updatedSlug = update.getSlug().toLowerCase().replaceAll(" ", "-");

            updatePost.setSlug(updatedSlug);
        }

        if(update.getDatePublished() != null && !update.getDatePublished().equals(updatePost.getDatePublished())) {
                updatePost.setDatePublished(update.getDatePublished());
        }

        if(update.getCover() != null && !update.getCover().equals(updatePost.getCover())) {
            updatePost.setCover(update.getCover());
        }

        if(update.getReadTime() != 0 && update.getReadTime() != updatePost.getReadTime()) {
            updatePost.setReadTime(update.getReadTime());
        }

        return postMapper.postToPostDto(postsRepository.save(updatePost));
    }

    @Override
    public PostsExcerptsPaged getPostsExcerpts(int page, int posts) {

        if(page > 0) {
            page--;
        }

        PageRequest pageRequest = PageRequest.of(page, posts, Sort.Direction.DESC, "dateCreated");

        List<PostExcerptDTO> postsList = postsRepository.findAll(pageRequest)
                .stream()
                .map(post -> postMapper.postToPostExcerptDto(post))
                .collect(Collectors.toList());

        return new PostsExcerptsPaged(getTotalPages(posts), postsList);
    }

    @Override
    public Resource getPostCover(String slug) {

        Optional<Post> post = postsRepository.findBySlug(slug);
        UrlResource resource= null;
        if(!post.isPresent()) {
            throw new ApiRequestException("Post not found", HttpStatus.NOT_FOUND);
        }

        Path filePath = Path.of(COVERS_PATH + "/" + post.get().getCover());

        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return resource;
    }


    /// Helper methods

    public String saveImage(String imageString, String fileName, String path) {
        String base64Image = imageString.split(",")[1];

        // Extracting extension from base64 string
        // First split gets data:[mime-type]
        // Second split gets extension
        String extension = imageString.split(";")[0].split("/")[1];

        String file = "";
        File directory = new File(path);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        byte[] imageBytes = Base64.decodeBase64(base64Image);


        try {

           file = fileName + "." + extension;

            FileOutputStream fstream = new FileOutputStream(new File(path + "/" + file));
            fstream.write(imageBytes);
            fstream.close();
        } catch (Exception ex) {
            System.out.println(ex);
        }
        return file;
    }

    public int getTotalPages(int posts) {
        return (int) Math.ceil((double) postsRepository.count() / posts);

    }
}
