package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class PostDTO {

    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private LocalDateTime datePublished;
    private String title;
    private String excerpt;
    private int readTime;
    private String cover;
    private String content;
    private String slug;
    private UserDTO author;
    private MetaDTO seo;
}
