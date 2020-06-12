package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.MetaDTO;
import com.bloggie.server.domain.Meta;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MetaMapper {

    MetaMapper INSTANCE = Mappers.getMapper(MetaMapper.class);
    Meta metaDtoToMeta(MetaDTO metaDTO);
    MetaDTO metaToMetaDto(Meta meta);

}
