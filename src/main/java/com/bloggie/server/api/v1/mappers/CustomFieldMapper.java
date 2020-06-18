package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.CustomFieldDTO;
import com.bloggie.server.domain.CustomField;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomFieldMapper {

    CustomFieldMapper INSTANCE = Mappers.getMapper(CustomFieldMapper.class);

    CustomField customFieldDtoToCustomField(CustomFieldDTO customFieldDTO);
    CustomFieldDTO customFieldToCustomFieldDto(CustomField customField);
}
