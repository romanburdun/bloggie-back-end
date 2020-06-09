package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
public class Page extends BaseEntity {

    private String title;
    private String content;
    private String slug;
}
