package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.MediaDTO;
import com.bloggie.server.security.responses.StateResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface MediaService {
    MediaDTO uploadFile(MultipartFile file);
    Resource getFile(String fileName);
    StateResponse deleteFile(String fileName);
}
