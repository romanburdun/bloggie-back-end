package com.bloggie.server.services;

import com.bloggie.server.domain.CustomFieldType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FilesService {
    String uploadFile(MultipartFile file);
    Resource getFile(String fileName, CustomFieldType type);
}
