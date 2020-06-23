package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.MediaMapper;
import com.bloggie.server.api.v1.models.MediaDTO;
import com.bloggie.server.domain.Media;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.repositories.CustomFieldsRepository;
import com.bloggie.server.repositories.MediaRepository;
import com.bloggie.server.repositories.PagesRepository;
import com.bloggie.server.repositories.PostsRepository;
import com.bloggie.server.security.responses.StateResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class MediaServiceImpl implements MediaService {
    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;
    private final PostsRepository postsRepository;
    private final PagesRepository pagesRepository;
    private final CustomFieldsRepository customFieldsRepository;

    @Value("${app.host}")
    private String domain;

    public MediaServiceImpl(MediaRepository mediaRepository,
                            MediaMapper mediaMapper,
                            PostsRepository postsRepository,
                            PagesRepository pagesRepository,
                            CustomFieldsRepository customFieldsRepository
    ) {
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
        this.postsRepository = postsRepository;
        this.pagesRepository = pagesRepository;
        this.customFieldsRepository = customFieldsRepository;
    }


    @Override
    public MediaDTO uploadFile(MultipartFile file) {

        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if(fileName.contains("..")) {
            throw new ApiRequestException("Bad file name.", HttpStatus.BAD_REQUEST);
        }

        String contentType = file.getContentType().split("/")[0];

        if(contentType.equals("application")) {
            contentType = "document";
        }

        File directory = new File("media/" + contentType);

        if(!directory.exists()) {
            directory.mkdirs();
        }

        Path filePath = Paths.get("./media/" + contentType);
        filePath = filePath.resolve(fileName);


        try {
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            System.out.println(ex);
            throw new ApiRequestException("Something went wrong while attempting to save file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        UriComponentsBuilder uri = UriComponentsBuilder.newInstance().scheme("http").host(domain).port(8080).path("/api/v1/media/" + fileName);
        Media newMedia = new Media();
        newMedia.setFileName(fileName);
        newMedia.setContentType(file.getContentType());
        newMedia.setSize(file.getSize());
        newMedia.setUrl(uri.toUriString());


        return mediaMapper.mediaToMediaDto(mediaRepository.save(newMedia));
    }

    @Override
    public Resource getFile(String fileName) {

        UrlResource resource= null;
        Path filePath = null;

        Media foundMedia = mediaRepository.findByFileName(fileName)
                .orElseThrow(()-> new ApiRequestException("File not found", HttpStatus.NOT_FOUND));

        String type = foundMedia.getContentType().split("/")[0];
        switch (type) {
            case "image":
                filePath = Path.of( "media/image/" + fileName);
                break;
            case "video":
                filePath = Path.of( "media/video/" + fileName);
                break;
            case "audio":
                filePath = Path.of( "media/audio/" + fileName);
                break;
            case "application":
                filePath = Path.of( "media/document/" + fileName);
                break;
            default:
                throw new ApiRequestException("Unsupported file type", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return resource;
    }

    @Override
    public StateResponse deleteFile(String fileName) {

        Media foundMedia = mediaRepository.findByFileName(fileName)
                .orElseThrow(()-> new ApiRequestException("File not found", HttpStatus.NOT_FOUND));

        Path filePath = null;
        String type = foundMedia.getContentType().split("/")[0];
        switch (type) {
            case "image":
                filePath = Path.of( "./media/image/" + fileName);
                break;
            case "video":
                filePath = Path.of( "./media/video/" + fileName);
                break;
            case "audio":
                filePath = Path.of( "./media/audio/" + fileName);
                break;
            case "application":
                filePath = Path.of( "./media/document/" + fileName);
                break;
            default:
                throw new ApiRequestException("Unsupported file type", HttpStatus.INTERNAL_SERVER_ERROR);

        }

        boolean isNotInPosts = postsRepository.findAllByMediaFileName(fileName).isEmpty();
        boolean isNotInPages = pagesRepository.findAllByMediaFileName(fileName).isEmpty();
        boolean isNotInCustomFields = customFieldsRepository.findAllByValue(foundMedia.getUrl()).isEmpty();

        if(isNotInPosts && isNotInPages && isNotInCustomFields) {
            File deleteFile = new File(filePath.toString());

            if(deleteFile.exists()) {
                deleteFile.delete();
            }

            mediaRepository.delete(foundMedia);
        } else {
            throw new ApiRequestException("File cannot be deleted because it is used somewhere in a website.", HttpStatus.EXPECTATION_FAILED);
        }

        return new StateResponse(true);
    }
}
