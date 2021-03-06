package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter

public class PostUpdateDTO {

    private LocalDateTime datePublished;
    private String cover;
    private String title;
    private String excerpt;
    private String content;
    private String slug;
    private int readTime;
    private MetaDTO seo;
    private boolean draft;
    private Set<MediaDTO> media;
}
