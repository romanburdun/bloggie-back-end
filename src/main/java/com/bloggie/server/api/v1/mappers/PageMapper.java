package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.PageDTO;
import com.bloggie.server.api.v1.models.PageUpdateDTO;
import com.bloggie.server.domain.Page;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PageMapper {

    PageMapper INSTANCE = Mappers.getMapper(PageMapper.class);

    Page pageDtoToPage(PageDTO pageDTO);
    Page pageUpdateDtoToPage(PageUpdateDTO pageUpdateDTO);
    PageDTO pageToPageDto(Page page);

}
