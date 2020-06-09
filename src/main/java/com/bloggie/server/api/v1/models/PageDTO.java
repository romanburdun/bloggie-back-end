package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageDTO {
    private String title;
    private String content;
    private String slug;
}
