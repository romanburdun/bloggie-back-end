package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.PageDTO;
import com.bloggie.server.api.v1.models.PageUpdateDTO;
import com.bloggie.server.domain.Page;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageMapperTest {

    private final PageMapper pageMapper = PageMapper.INSTANCE;

    @Test
    void pageDtoToPage() {

        PageDTO pageDTO = new PageDTO();
        pageDTO.setTitle("Test page title");
        pageDTO.setContent("<p>Test page content</p>");
        pageDTO.setSlug("test-page");

        Page page = pageMapper.pageDtoToPage(pageDTO);

        assertNotNull(page);
        assertEquals("Test page title", page.getTitle());
        assertEquals("<p>Test page content</p>", page.getContent());
        assertEquals("test-page", page.getSlug());
    }

    @Test
    void pageToPageDto() {

        Page page = new Page();
        page.setTitle("Test page title");
        page.setContent("<p>Test page content</p>");
        page.setSlug("test-page");

        PageDTO pageDTO = pageMapper.pageToPageDto(page);

        assertNotNull(pageDTO);
        assertEquals("Test page title", pageDTO.getTitle());
        assertEquals("<p>Test page content</p>", pageDTO.getContent());
        assertEquals("test-page", pageDTO.getSlug());
    }

    @Test
    void pageUpdateDtoToPage() {

        PageUpdateDTO pageUpdateDTO = new PageUpdateDTO();
        pageUpdateDTO.setTitle("Test page title");
        pageUpdateDTO.setContent("<p>Test page content</p>");
        pageUpdateDTO.setSlug("test-page");

        Page page = pageMapper.pageUpdateDtoToPage(pageUpdateDTO);

        assertNotNull(page);
        assertEquals("Test page title", page.getTitle());
        assertEquals("<p>Test page content</p>", page.getContent());
        assertEquals("test-page", page.getSlug());
    }
}
