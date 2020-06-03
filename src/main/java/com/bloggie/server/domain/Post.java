package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
public class Post extends BaseEntity {

    private String postTitle;
    private int readTime;
    private String cover;
    private String content;
}
