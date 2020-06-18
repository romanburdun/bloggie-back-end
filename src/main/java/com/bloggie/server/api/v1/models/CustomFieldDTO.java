package com.bloggie.server.api.v1.models;

import com.bloggie.server.domain.CustomFieldType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
public class CustomFieldDTO {
    private String fieldName;
    private String value;
    @Enumerated(value = EnumType.STRING)
    private CustomFieldType type;
}
