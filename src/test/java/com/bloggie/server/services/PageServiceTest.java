package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.PageMapper;
import com.bloggie.server.api.v1.models.PageDTO;
import com.bloggie.server.api.v1.models.PageUpdateDTO;
import com.bloggie.server.domain.Page;
import com.bloggie.server.fixtures.TestFixtures;
import com.bloggie.server.repositories.PagesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

class PageServiceTest {

    @Mock
    private PageService pageService;
    @Mock
    private PagesRepository pagesRepository;
    private PageMapper pageMapper = PageMapper.INSTANCE;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        pageService = new PageServiceImpl(pagesRepository, pageMapper);
    }

    @Test
    void createPage() {

        Page newPage = new Page();
        newPage.setTitle("Test page");
        newPage.setContent("<div>Test page content</div>");
        newPage.setSlug("test-page");

        Mockito.when(pagesRepository.save(any(Page.class))).thenReturn(TestFixtures.getSinglePage());

        PageDTO createdPage = pageService.createPage(pageMapper.pageToPageDto(newPage));

        assertNotNull(createdPage);
        assertEquals("test-page", createdPage.getSlug());
    }

    @Test
    void getPageBySlug() {

        Mockito.when(pagesRepository.findBySlug(any(String.class)))
                .thenReturn(Optional.of(TestFixtures.getSinglePage()));

        PageDTO foundPage = pageService.getPageBySlug("test-page");

        assertNotNull(foundPage);
        assertEquals("test-page", foundPage.getSlug());

    }

    @Test
    void deletePageBySlug() {

        Mockito.when(pagesRepository.findBySlug(any(String.class)))
                .thenReturn(Optional.of(TestFixtures.getSinglePage()));

        PageDTO deletedPage = pageService.deletePageBySlug("test-page");

        assertNotNull(deletedPage);
        assertEquals("test-page", deletedPage.getSlug());
    }

    @Test
    void updatePageBySlug() {

        PageUpdateDTO update = new PageUpdateDTO();
        update.setTitle("Test page updated");
        update.setContent("<div>Test page content updated</div>");
        update.setSlug("test-page");

        Mockito.when(pagesRepository.findBySlug(anyString())).thenReturn(Optional.of(TestFixtures.getSinglePage()));
        Mockito.when(pagesRepository.save(any(Page.class))).thenReturn(TestFixtures.getUpdatedPage());

        PageDTO updatedPage = pageService.updatePageBySlug("test-page", update);


        assertNotNull(updatedPage);
        assertEquals("<div>Test page content updated</div>", updatedPage.getContent());
    }

    @Test
    void getAllPages() {

        Mockito.when(pagesRepository.findAll()).thenReturn(TestFixtures.getPages());

        List<PageDTO> pages = pageService.getAllPages();

        assertNotNull(pages);
        assertEquals(2, pages.size());
    }
}
