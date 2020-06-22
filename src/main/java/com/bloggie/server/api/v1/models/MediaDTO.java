package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MediaDTO {
    private String contentType;
    private String fileName;
    private long size;
    private String url;
}
