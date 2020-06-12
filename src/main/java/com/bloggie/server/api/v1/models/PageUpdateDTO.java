package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageUpdateDTO {
    private String title;
    private String content;
    private String slug;
    private MetaDTO seo;
}
