package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class PostExcerptDTO {
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private LocalDateTime publicationDate;
    private String excerpt;
    private String slug;
    private String postTitle;
    private String readTime;

}
