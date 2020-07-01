package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class PageReaderDTO {
    private String title;
    private String content;
    private String slug;
    private MetaDTO seo;
    List<CustomFieldDTO> customFields;

}
