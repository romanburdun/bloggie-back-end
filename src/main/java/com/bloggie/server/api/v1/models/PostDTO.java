package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostDTO {

    private Long id;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String postTitle;
    private int readTime;
    private String cover;
    private String content;
}
