package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.CustomFieldMapper;
import com.bloggie.server.api.v1.mappers.MediaMapper;
import com.bloggie.server.api.v1.mappers.MetaMapper;
import com.bloggie.server.api.v1.mappers.PageMapper;
import com.bloggie.server.api.v1.models.PageDTO;
import com.bloggie.server.api.v1.models.PageUpdateDTO;
import com.bloggie.server.domain.Meta;
import com.bloggie.server.domain.Page;
import com.bloggie.server.fixtures.MetaFixtures;
import com.bloggie.server.fixtures.PagesFixtures;
import com.bloggie.server.repositories.CustomFieldsRepository;
import com.bloggie.server.repositories.MediaRepository;
import com.bloggie.server.repositories.MetasRepository;
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
    @Mock
    private MetasRepository metasRepository;
    @Mock
    private CustomFieldsRepository customFieldsRepository;
    @Mock
    private MediaRepository mediaRepository;

    private MetaMapper metaMapper = MetaMapper.INSTANCE;
    private PageMapper pageMapper = PageMapper.INSTANCE;
    private CustomFieldMapper customFieldMapper = CustomFieldMapper.INSTANCE;
    private MediaMapper mediaMapper = MediaMapper.INSTANCE;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        pageService = new PageServiceImpl(pagesRepository,
                pageMapper,
                metasRepository,
                metaMapper,
                customFieldMapper,
                customFieldsRepository,
                mediaMapper,
                mediaRepository
        );
    }

    @Test
    void createPage() {

        Page newPage = new Page();
        newPage.setTitle("Test page");
        newPage.setContent("<div>Test page content</div>");
        newPage.setSlug("test-page");

        Meta pageMeta = new Meta();
        pageMeta.setSeoTitle("Test page SEO title");
        pageMeta.setSeoDescription("Test page SEO description");

        newPage.setSeo(pageMeta);

        Mockito.when(metasRepository.save(any(Meta.class))).thenReturn(MetaFixtures.getPageMeta());
        Mockito.when(pagesRepository.save(any(Page.class))).thenReturn(PagesFixtures.getSinglePage());


        PageDTO createdPage = pageService.createPage(pageMapper.pageToPageDto(newPage));

        assertNotNull(createdPage);
        assertEquals("test-page", createdPage.getSlug());
    }

    @Test
    void getPageBySlug() {

        Mockito.when(pagesRepository.findBySlug(any(String.class)))
                .thenReturn(Optional.of(PagesFixtures.getSinglePage()));

        PageDTO foundPage = pageService.getPageBySlug("test-page");

        assertNotNull(foundPage);
        assertEquals("test-page", foundPage.getSlug());

    }

    @Test
    void deletePageBySlug() {

        Mockito.when(pagesRepository.findBySlug(any(String.class)))
                .thenReturn(Optional.of(PagesFixtures.getSinglePage()));

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

        Mockito.when(pagesRepository.findBySlug(anyString())).thenReturn(Optional.of(PagesFixtures.getSinglePage()));
        Mockito.when(pagesRepository.save(any(Page.class))).thenReturn(PagesFixtures.getUpdatedPage());

        PageDTO updatedPage = pageService.updatePageBySlug("test-page", update);


        assertNotNull(updatedPage);
        assertEquals("<div>Test page content updated</div>", updatedPage.getContent());
    }

    @Test
    void getAllPages() {

        Mockito.when(pagesRepository.findAll()).thenReturn(PagesFixtures.getPages());

        List<PageDTO> pages = pageService.getAllPages();

        assertNotNull(pages);
        assertEquals(2, pages.size());
    }
}
