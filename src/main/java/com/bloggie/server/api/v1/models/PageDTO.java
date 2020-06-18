package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class PageDTO {
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String title;
    private String content;
    private String slug;
    private MetaDTO seo;
    List<CustomFieldDTO> customFields;

}
