package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.PageDTO;

import java.util.List;

public interface PageService {
    PageDTO createPage(PageDTO pageDTO);
    PageDTO getPageBySlug(String slug);
    PageDTO deletePageBySlug(String slug);
    PageDTO updatePageBySlug(String slug, PageDTO updateDTO);
    List<PageDTO> getAllPages();
}
