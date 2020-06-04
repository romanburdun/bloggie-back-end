package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Entity;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class Post extends BaseEntity {

    private LocalDateTime publicationDate;
    private String title;
    private String cover;
    private String content;
    private String excerpt;
    private String slug;
    private int readTime;
}
