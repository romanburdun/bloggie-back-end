package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.MediaDTO;
import com.bloggie.server.domain.Media;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MediaMapper {

    MediaMapper INSTANCE = Mappers.getMapper(MediaMapper.class);

    MediaDTO mediaToMediaDto(Media mediaData);
    Media mediaDtoToMedia(MediaDTO mediaDataDTO);
}
