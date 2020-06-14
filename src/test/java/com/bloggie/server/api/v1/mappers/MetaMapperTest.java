package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.MetaDTO;
import com.bloggie.server.domain.Meta;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MetaMapperTest {

    private final MetaMapper metaMapper = MetaMapper.INSTANCE;

    @Test
    void metaDtoToMeta() {

        MetaDTO metaDTO = new MetaDTO();

        metaDTO.setSeoTitle("Test seo title");
        metaDTO.setSeoDescription("Test seo description");

        Meta meta = metaMapper.metaDtoToMeta(metaDTO);

        assertNotNull(meta);
        assertEquals("Test seo title", meta.getSeoTitle());
        assertEquals("Test seo description", meta.getSeoDescription());

    }

    @Test
    void metaToMetaDto() {

        Meta meta = new Meta();

        meta.setSeoTitle("Test seo title");
        meta.setSeoDescription("Test seo description");

        MetaDTO metaDTO = metaMapper.metaToMetaDto(meta);

        assertNotNull(metaDTO);
        assertEquals("Test seo title", metaDTO.getSeoTitle());
        assertEquals("Test seo description", metaDTO.getSeoDescription());
    }
}
