package com.bloggie.server.services;

import com.bloggie.server.domain.CustomFieldType;
import com.bloggie.server.exceptions.ApiRequestException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class MediaServiceImpl implements MediaService {
    @Override
    public String uploadFile(MultipartFile file) {

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

        return fileName;
    }

    @Override
    public Resource getFile(String fileName, CustomFieldType type) {

        UrlResource resource= null;
        Path filePath = null;

        switch (type) {
            case IMAGE:
                filePath = Path.of( "media/image/" + fileName);
                break;
            case VIDEO:
                filePath = Path.of( "media/video/" + fileName);
                break;
            case AUDIO:
                filePath = Path.of( "media/audio/" + fileName);
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
}
