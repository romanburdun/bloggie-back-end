package com.bloggie.server.controllers;

import com.bloggie.server.services.FilesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/media")
@AllArgsConstructor
public class MediaController {
    private final FilesService filesService;

    @PostMapping("upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        return filesService.uploadFile(file);
    }

}
