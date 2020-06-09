package com.bloggie.server.controllers;

import com.bloggie.server.api.v1.models.PageDTO;
import com.bloggie.server.api.v1.models.PageUpdateDTO;
import com.bloggie.server.fixtures.TestFixtures;
import com.bloggie.server.services.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PageControllerTest extends AsJsonController {

    @Mock
    private PageService pageService;

    @InjectMocks
    private PageController pageController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(pageController).build();
    }

    @Test
    public void createPage() throws Exception {

        PageDTO newPage = new PageDTO();
        newPage.setTitle("Test page");
        newPage.setContent("<div>Test page content</div>");
        newPage.setSlug("test-page");

        Mockito.when(pageService.createPage(any(PageDTO.class))).thenReturn(TestFixtures.getSinglePageDTO());

        mockMvc.perform(post("/api/v1/pages").contentType(MediaType.APPLICATION_JSON).content(asJsonString(newPage)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


    }

    @Test
    public void updatePage() throws Exception {

        PageUpdateDTO update = new PageUpdateDTO();
        update.setTitle("Test page updated");
        update.setContent("<div>Test page content updated</div>");
        update.setSlug("test-page");

        Mockito.when(pageService.updatePageBySlug("test-page", update)).thenReturn(TestFixtures.getUpdatedPageDTO());

        mockMvc.perform(put("/api/v1/pages/test-page").contentType(MediaType.APPLICATION_JSON).content(asJsonString(update)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    public void getPagesBySlug() throws Exception {

        Mockito.when(pageService.getPageBySlug(any(String.class))).thenReturn(TestFixtures.getSinglePageDTO());

        mockMvc.perform(get("/api/v1/pages/test-page"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

    }


    @Test
    public void getPages() throws Exception {

        Mockito.when(pageService.getAllPages()).thenReturn(TestFixtures.getPagesDTOs());

        mockMvc.perform(get("/api/v1/pages"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

    }
}
