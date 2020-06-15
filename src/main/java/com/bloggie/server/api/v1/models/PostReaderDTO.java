package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Setter
@Getter
public class PostReaderDTO {
    private LocalDateTime datePublished;
    private LocalDateTime dateUpdated;
    private String title;
    private int readTime;
    private String content;
    private String slug;
    private UserDTO author;
    private MetaDTO seo;
}
