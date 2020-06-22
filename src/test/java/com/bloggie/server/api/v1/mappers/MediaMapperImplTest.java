package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.MediaDTO;
import com.bloggie.server.domain.Media;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MediaMapperImplTest {

    private final MediaMapper mediaMapper= MediaMapper.INSTANCE;


    @Test
    void mediaToMediaDto() {
        Media media = new Media();
        media.setFileName("test.png");
        media.setContentType("image/png");
        media.setSize(254667);
        media.setUrl("http://localhost:8080/api/v1/media/test.png");

        MediaDTO mediaDTO = mediaMapper.mediaToMediaDto(media);
        assertNotNull(mediaDTO);
        assertEquals(254667, mediaDTO.getSize());
        assertEquals("image/png", mediaDTO.getContentType());
        assertEquals("test.png", mediaDTO.getFileName());
    }

    @Test
    void mediaDtoToMedia() {

        MediaDTO mediaDTO = new MediaDTO();
        mediaDTO.setFileName("test.png");
        mediaDTO.setContentType("image/png");
        mediaDTO.setSize(254667);
        mediaDTO.setUrl("http://localhost:8080/api/v1/media/test.png");

        Media media = mediaMapper.mediaDtoToMedia(mediaDTO);
        assertNotNull(mediaDTO);
        assertEquals(254667, media.getSize());
        assertEquals("image/png", media.getContentType());
        assertEquals("test.png", media.getFileName());
    }
}