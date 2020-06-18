package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.CustomFieldDTO;
import com.bloggie.server.domain.CustomField;
import com.bloggie.server.domain.CustomFieldType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomFieldMapperTest {

    private final CustomFieldMapper fieldMapper = CustomFieldMapper.INSTANCE;

    @Test
    void customFieldDtoToCustomField() {

        CustomFieldDTO fieldDTO = new CustomFieldDTO();
        fieldDTO.setFieldName("hero_image");
        fieldDTO.setType(CustomFieldType.IMAGE);
        fieldDTO.setValue("hero_background.webp");

        CustomField field = fieldMapper.customFieldDtoToCustomField(fieldDTO);

        assertNotNull(field);
        assertEquals(CustomFieldType.IMAGE, field.getType());
        assertEquals("hero_background.webp", field.getValue());
        assertEquals("hero_image", field.getFieldName());
    }

    @Test
    void customFieldToCustomFieldDto() {

        CustomField field = new CustomField();
        field.setFieldName("about_image");
        field.setType(CustomFieldType.IMAGE);
        field.setValue("about_us.webp");

        CustomFieldDTO fieldDTO = fieldMapper.customFieldToCustomFieldDto(field);

        assertNotNull(fieldDTO);
        assertEquals(CustomFieldType.IMAGE, fieldDTO.getType());
        assertEquals("about_us.webp", fieldDTO.getValue());
        assertEquals("about_image", fieldDTO.getFieldName());
    }
}
