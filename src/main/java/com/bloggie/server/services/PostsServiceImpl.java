package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.MediaMapper;
import com.bloggie.server.api.v1.mappers.MetaMapper;
import com.bloggie.server.api.v1.mappers.PostMapper;
import com.bloggie.server.api.v1.models.*;
import com.bloggie.server.domain.*;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.misc.PostsExcerptsPaged;
import com.bloggie.server.misc.PostsPaged;
import com.bloggie.server.repositories.MediaRepository;
import com.bloggie.server.repositories.MetasRepository;
import com.bloggie.server.repositories.PostsRepository;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PostsServiceImpl implements PostsService {

    private final PostsRepository postsRepository;
    private final PostMapper postMapper;
    private final AuthService authService;
    private final MetaMapper metaMapper;
    private final MetasRepository metasRepository;
    private final MediaRepository mediaRepository;

    @Override
    public PostDTO createPost(PostDTO postDTO) {

        if(postDTO.getTitle() == null || postDTO.getContent() == null ) {
            throw new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST);
        }

        if(postDTO.getTitle().equals("") || postDTO.getContent().equals("")) {
            throw new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST);
        }

        if(postDTO.getDatePublished() == null || postDTO.getDatePublished().equals("")) {
            postDTO.setDatePublished(LocalDateTime.now());
        }


        String slug;
        if (postDTO.getSlug() == null || postDTO.getSlug().equals("")) {

            slug = postDTO.getTitle().toLowerCase().replaceAll(" ", "-");
        } else {
            slug = postDTO.getSlug().toLowerCase().replaceAll(" ", "-");
        }

        postDTO.setSlug(slug);

        if(postDTO.getCover() == null) {
            postDTO.setCover("");
        }


        Post newPost = postMapper.postDtoToPost(postDTO);

        Meta savedMeta = metasRepository.save(metaMapper.metaDtoToMeta(postDTO.getSeo()));
        newPost.setSeo(savedMeta);
        User user = authService.getRequestUser().orElseThrow(() -> new ApiRequestException("Not Authorized", HttpStatus.UNAUTHORIZED));

        newPost.setAuthor(user);


        if( postDTO.getMedia() != null && postDTO.getMedia().size() != 0) {

            Set<Media> pageMedia = new HashSet<>();

            for (MediaDTO mediaDTO : postDTO.getMedia()) {
                Media foundMedia = mediaRepository.findByFileName(mediaDTO.getFileName())
                        .orElseThrow(()-> new ApiRequestException("Media data not found", HttpStatus.NOT_FOUND));
                pageMedia.add(foundMedia);
            }

            newPost.setMedia(pageMedia);
        }

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
    public PostReaderDTO getPostBySlug(String slug) {

        Post post = postsRepository.findBySlugAndDraftIsFalse(slug)
                .orElseThrow(() -> new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));
        return postMapper.postToPostReaderDTO(post);
    }

    @Override
    public PostDTO deletePostBySlug(String slug) {

        User user = authService.getRequestUser()
                .orElseThrow(()-> new ApiRequestException("Not authenticated", HttpStatus.UNAUTHORIZED));


        Post post =  postsRepository.findBySlug(slug)
                .orElseThrow(()-> new ApiRequestException("Post not found", HttpStatus.NOT_FOUND));

        if(post.getAuthor().getId() != user.getId()) {
           throw new ApiRequestException("Not authorized", HttpStatus.FORBIDDEN);
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

            if(!update.getTitle().equals("") && update.getTitle() != null
                    && !update.getTitle().equals(foundPost.getTitle())) {
                foundPost.setTitle(update.getTitle());
            }


            if(!update.getContent().equals("") && update.getContent() != null
                    && !update.getContent().equals(foundPost.getContent())) {
                foundPost.setContent(update.getContent());
            }

            if(!update.getSlug().equals("") && update.getSlug() != null
                    && !update.getSlug().equals(foundPost.getSlug())) {
                String updatedSlug = update.getSlug().toLowerCase().replaceAll(" ", "-");
                foundPost.setSlug(updatedSlug);
            }


            if(update.getDatePublished() != null
                    && !update.getDatePublished().equals(foundPost.getDatePublished())) {
                foundPost.setDatePublished(update.getDatePublished());
            }

            if(update.getCover() != null && !update.getCover().equals(foundPost.getCover())) {
                foundPost.setCover(update.getCover());
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

            foundPost.setDraft(update.isDraft());

        }

        if( update.getMedia() != null && update.getMedia().size() != 0) {

            Set<Media> pageMedia = new HashSet<>();

            for (MediaDTO mediaDTO : update.getMedia()) {
                Media foundMedia = mediaRepository.findByFileName(mediaDTO.getFileName())
                        .orElseThrow(()-> new ApiRequestException("Media data not found", HttpStatus.NOT_FOUND));
                pageMedia.add(foundMedia);
            }

            foundPost.setMedia(pageMedia);
        }
        return postMapper.postToPostDto(postsRepository.save(foundPost));
    }

    @Override
    public PostsExcerptsPaged getPostsExcerpts(int page, int posts) {

        if(page > 0) {
            page--;
        }


        PageRequest pageRequest = PageRequest.of(page, posts, Sort.Direction.DESC, "datePublished");

        LocalDateTime today = LocalDateTime.now();


        List<PostExcerptDTO> postsList = postsRepository.findAllByDatePublishedBeforeAndDraftIsFalse(today, pageRequest)
                .stream()
                .map(post -> postMapper.postToPostExcerptDto(post))
                .collect(Collectors.toList());



        return new PostsExcerptsPaged(getTotalPages(posts), postsList);
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


    public int getTotalPages(int posts) {
        return (int) Math.ceil((double) postsRepository.count() / posts);

    }
}
