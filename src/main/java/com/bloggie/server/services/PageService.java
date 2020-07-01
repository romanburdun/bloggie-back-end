package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.PageDTO;
import com.bloggie.server.api.v1.models.PageReaderDTO;
import com.bloggie.server.api.v1.models.PageUpdateDTO;

import java.util.List;

public interface PageService {
    PageDTO createPage(PageDTO pageDTO);
    PageReaderDTO getPageBySlug(String slug);
    PageDTO deletePageBySlug(String slug);
    PageDTO updatePageBySlug(String slug, PageUpdateDTO updateDTO);
    List<PageDTO> getAllPages();
}
