package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.MetaMapper;
import com.bloggie.server.api.v1.mappers.PostMapper;
import com.bloggie.server.api.v1.models.MetaDTO;
import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostExcerptDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.domain.*;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.misc.PostsExcerptsPaged;
import com.bloggie.server.misc.PostsPaged;
import com.bloggie.server.repositories.MetasRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;
    private final PostMapper postMapper;
    private final AuthService authService;
    private final MetaMapper metaMapper;
    private final MetasRepository metasRepository;
    private final String COVERS_PATH = "images/covers";

    @Override
    public PostDTO createPost(PostDTO postDTO) {

        if(postDTO.getTitle() == null || postDTO.getContent() == null ) {
            throw new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST);
        }

        if(postDTO.getContent() == "" || postDTO.getContent() == "") {
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


        Post newPost = postMapper.postDtoToPost(postDTO);

        Meta savedMeta = metasRepository.save(metaMapper.metaDtoToMeta(postDTO.getSeo()));
        newPost.setSeo(savedMeta);
        User user = authService.getRequestUser().orElseThrow(() -> new ApiRequestException("Not Authorized", HttpStatus.UNAUTHORIZED));

        newPost.setAuthor(user);

        Post createdPost = postsRepository.save(newPost);


        return postMapper.postToPostDto(createdPost);
    }

    @Override
    public PostsPaged getPosts(int page, int posts) {

        if(page > 0) {
            page--;
        }

        User user = authService.getRequestUser()
                .orElseThrow(()-> new ApiRequestException("Not authenticated", HttpStatus.UNAUTHORIZED));


        Role adminRole = new Role();
        adminRole.setName(RoleName.ROLE_ADMINISTRATOR);

        List<PostDTO> postsList;
        PageRequest pageRequest = PageRequest.of(page, posts, Sort.Direction.DESC, "dateCreated");


        for(Role role : user.getRoles()) {
            if(adminRole.getName() == role.getName()) {
                postsList = postsRepository.findAll(pageRequest)
                        .stream()
                        .map(post -> postMapper.postToPostDto(post))
                        .collect(Collectors.toList());

                return new PostsPaged(getTotalPages(posts), postsList);
            }
        }


        postsList = postsRepository.findAllByAuthor(user,pageRequest)
                .stream()
                .map(post -> postMapper.postToPostDto(post))
                .collect(Collectors.toList());
        return new PostsPaged(getTotalPages(posts), postsList);


    }

    @Override
    public PostDTO getPostBySlug(String slug) {

        Post post = postsRepository.findBySlug(slug)
                .orElseThrow(() -> new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));
        return postMapper.postToPostDto(post);
    }

    @Override
    public PostDTO deletePostBySlug(String slug) {

        Post post =  postsRepository.findBySlug(slug)
                .orElseThrow(()-> new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));


        File deleteCover = new File(COVERS_PATH + "/" + post.getCover());

        if(deleteCover.exists()) {
            deleteCover.delete();
        }
        postsRepository.deleteById(post.getId());

        return postMapper.postToPostDto(post);
    }

    @Override
    public PostDTO updatePostBySlug( String slug, PostUpdateDTO update) {

        User user = authService.getRequestUser()
                .orElseThrow(()-> new ApiRequestException("Not authenticated", HttpStatus.UNAUTHORIZED));

        Post foundPost = postsRepository.findBySlug(slug)
                .orElseThrow(()-> new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));

        if(foundPost.getAuthor().getId() != user.getId()) {
            throw new ApiRequestException("Unauthorized request", HttpStatus.FORBIDDEN);
        }

        if(!foundPost.equals(postMapper.postUpdateDtoToPost(update))) {

            if(update.getTitle() != "" && update.getTitle() != null
                    && !update.getTitle().equals(foundPost.getTitle())) {
                foundPost.setTitle(update.getTitle());
            }


            if(update.getContent() != null && update.getContent() != ""
                    && !update.getContent().equals(foundPost.getContent())) {
                foundPost.setContent(update.getContent());
            }

            if(update.getSlug() != "" && update.getSlug() != null
                    && !update.getSlug().equals(foundPost.getSlug())) {
                String updatedSlug = update.getSlug().toLowerCase().replaceAll(" ", "-");
                foundPost.setSlug(updatedSlug);
            }


            if(update.getDatePublished() != null
                    && !update.getDatePublished().equals(foundPost.getDatePublished())) {
                foundPost.setDatePublished(update.getDatePublished());
            }

            if(update.getCover() != null && !update.getCover().equals(foundPost.getCover())) {
                if(update.getCover().startsWith("data:")) {
                    foundPost.setCover(saveImage(update.getCover(), slug, COVERS_PATH));
                } else {
                    foundPost.setCover("");
                }
            }

            if(update.getReadTime() != 0) {
                foundPost.setReadTime(update.getReadTime());
            }

            if(update.getSeo() != null) {
                MetaDTO seoDTO = update.getSeo();
                Meta seo = foundPost.getSeo();
                if(seoDTO.getSeoTitle() != null && !seo.getSeoTitle().equals(seoDTO.getSeoTitle())) {
                    seo.setSeoTitle(seoDTO.getSeoTitle());
                }

                if(seoDTO.getSeoDescription() != null && !seo.getSeoDescription().equals(seoDTO.getSeoDescription())) {
                    seo.setSeoDescription(seoDTO.getSeoDescription());
                }

                foundPost.setSeo(metasRepository.save(seo));
            }

            return postMapper.postToPostDto(postsRepository.save(foundPost));
        }

        return postMapper.postToPostDto(foundPost);
    }

    @Override
    public PostsExcerptsPaged getPostsExcerpts(int page, int posts) {

        if(page > 0) {
            page--;
        }


        PageRequest pageRequest = PageRequest.of(page, posts, Sort.Direction.DESC, "datePublished");

        LocalDateTime today = LocalDateTime.now();


        List<PostExcerptDTO> postsList = postsRepository.findAllByDatePublishedBefore(today, pageRequest)
                .stream()
                .map(post -> postMapper.postToPostExcerptDto(post))
                .collect(Collectors.toList());



        return new PostsExcerptsPaged(getTotalPages(posts), postsList);
    }

    @Override
    public Resource getPostCover(String slug) {

        Post post = postsRepository.findBySlug(slug)
                .orElseThrow(()-> new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));

        UrlResource resource= null;


        Path filePath = Path.of(COVERS_PATH + "/" + post.getCover());

        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }


        return resource;
    }

    @Override
    public List<PostExcerptDTO> searchPostsByTitle(String searchTitle) {


        LocalDateTime now = LocalDateTime.now();

        List<PostExcerptDTO> foundPosts = postsRepository.findAllByTitleIgnoreCaseContaining(searchTitle)
                .stream()
                .map(post -> postMapper.postToPostExcerptDto(post))
                .collect(Collectors.toList());

        List<PostExcerptDTO> publishedPosts = new ArrayList<>();

        for(PostExcerptDTO pe : foundPosts) {
            if(pe.getDatePublished().isBefore(now)) {
                publishedPosts.add(pe);
            }
        }


            return publishedPosts;
    }

    @Override
    public List<PostExcerptDTO> searchPostsByContent(String searchContent) {

        LocalDateTime now = LocalDateTime.now();



        List<PostExcerptDTO> foundPosts = postsRepository.findAllByContentIgnoreCaseContaining(searchContent)
                .stream()
                .map(post -> postMapper.postToPostExcerptDto(post))
                .collect(Collectors.toList());

        List<PostExcerptDTO> publishedPosts = new ArrayList<>();

        for(PostExcerptDTO pe : foundPosts) {
            if(pe.getDatePublished().isBefore(now)) {
                publishedPosts.add(pe);
            }
        }


        return publishedPosts;

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
