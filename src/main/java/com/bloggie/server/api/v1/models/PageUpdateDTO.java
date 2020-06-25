package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Setter
@Getter
public class PageUpdateDTO {
    private String title;
    private String content;
    private String slug;
    private MetaDTO seo;
    private List<CustomFieldDTO> customFields;
    private Set<MediaDTO> media;
}
