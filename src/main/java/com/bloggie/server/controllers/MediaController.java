package com.bloggie.server.controllers;

import com.bloggie.server.api.v1.models.MediaDTO;
import com.bloggie.server.security.responses.AuthResponse;
import com.bloggie.server.services.MediaService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/media")
@AllArgsConstructor
public class MediaController {
    private final MediaService mediaService;

    @PostMapping("/upload")
    public MediaDTO uploadFile(@RequestParam("file") MultipartFile file) {
        return mediaService.uploadFile(file);
    }

    @GetMapping(value="/{fileName}",produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Resource getFile(@PathVariable String fileName) {
        return mediaService.getFile(fileName);
    }

    @DeleteMapping("/{fileName}")
    public AuthResponse deleteFile(@PathVariable String fileName) {
        return mediaService.deleteFile(fileName);
    }
}
