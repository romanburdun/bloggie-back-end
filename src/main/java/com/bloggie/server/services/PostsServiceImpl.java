package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.PostMapper;
import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostExcerptDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.domain.Post;
import com.bloggie.server.repositories.PostsRepository;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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

        if(postDTO.getDatePublished() == null) {
            postDTO.setDatePublished(LocalDateTime.now());
        }



        String slug = postDTO.getSlug().toLowerCase().replaceAll(" ", "-");
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
    public List<PostExcerptDTO> getPostsExcerpts() {

        return postsRepository.findAll()
                .stream()
                .map(post -> postMapper.postToPostExcerptDto(post))
                .collect(Collectors.toList());
    }

    @Override
    public Resource getPostCover(String slug) {

        Optional<Post> post = postsRepository.findBySlug(slug);
        UrlResource resource= null;
        if(post.isPresent()) {
            Path filePath = Path.of(COVERS_PATH + "/" + post.get().getCover());

            try {
                resource = new UrlResource(filePath.toUri());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
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
}
