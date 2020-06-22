package com.bloggie.server.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Setter
@Getter
public class Media extends BaseEntity{
    private String contentType;
    private String fileName;
    private String size;
    private String url;
}
