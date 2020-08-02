package com.bloggie.server.controllers;

import com.bloggie.server.api.v1.models.PageDTO;
import com.bloggie.server.api.v1.models.PageUpdateDTO;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.fixtures.AuthTestConfig;
import com.bloggie.server.fixtures.PagesFixtures;
import com.bloggie.server.services.PageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(classes = AuthTestConfig.class,webEnvironment = SpringBootTest.WebEnvironment.MOCK )
@ExtendWith(MockitoExtension.class)
class PageControllerProtectedTest extends AsJsonController {

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private PageService pageService;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails("jadmin@test.com")
    public void createPageAuthenticated() throws Exception {

        PageDTO newPage = new PageDTO();
        newPage.setTitle("Test page");
        newPage.setContent("<div>Test page content</div>");
        newPage.setSlug("test-page");

        Mockito.when(pageService.createPage(any(PageDTO.class))).thenReturn(PagesFixtures.getSinglePageDTO());

        mockMvc.perform(post("/api/v1/pages").contentType(MediaType.APPLICATION_JSON).content(asJsonString(newPage)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


    }

    @Test
    @WithAnonymousUser
    public void createPageNotAuthenticated() throws Exception {

        PageDTO newPage = new PageDTO();
        newPage.setTitle("Test page");
        newPage.setContent("<div>Test page content</div>");
        newPage.setSlug("test-page");

        Mockito.when(pageService.createPage(any(PageDTO.class))).thenReturn(PagesFixtures.getSinglePageDTO());

        mockMvc.perform(post("/api/v1/pages").contentType(MediaType.APPLICATION_JSON).content(asJsonString(newPage)))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();


    }

    @Test
    @WithUserDetails("jadmin@test.com")
    public void createPageBadRequest() throws Exception {

        PageDTO newPage = new PageDTO();
        newPage.setTitle("Test page");
        newPage.setSlug("test-page");

        Mockito.when(pageService.createPage(any(PageDTO.class))).thenThrow(new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/api/v1/pages").contentType(MediaType.APPLICATION_JSON).content(asJsonString(newPage)))
                .andExpect(status().isBadRequest())
                .andExpect(result-> assertTrue(result.getResolvedException() instanceof ApiRequestException))
                .andExpect(result -> assertEquals("Bad request", result.getResolvedException().getMessage()))
                .andReturn()
                .getResponse()
                .getContentAsString();


    }


    @Test
    @WithUserDetails("jadmin@test.com")
    public void updatePageAuthenticated() throws Exception {

        PageUpdateDTO update = new PageUpdateDTO();
        update.setTitle("Test page updated");
        update.setContent("<div>Test page content updated</div>");
        update.setSlug("test-page");

        Mockito.when(pageService.updatePageBySlug(any(String.class), any(PageUpdateDTO.class))).thenReturn(PagesFixtures.getUpdatedPageDTO());

        mockMvc.perform(put("/api/v1/pages/test-page").contentType(MediaType.APPLICATION_JSON).content(asJsonString(update)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @WithAnonymousUser
    public void updatePageNotAuthenticated() throws Exception {

        PageUpdateDTO update = new PageUpdateDTO();
        update.setTitle("Test page updated");
        update.setContent("<div>Test page content updated</div>");
        update.setSlug("test-page");

        Mockito.when(pageService.updatePageBySlug(any(String.class), any(PageUpdateDTO.class))).thenReturn(PagesFixtures.getUpdatedPageDTO());

        mockMvc.perform(put("/api/v1/pages/test-page").contentType(MediaType.APPLICATION_JSON).content(asJsonString(update)))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    @WithUserDetails("jadmin@test.com")
    public void updatePageBadRequest() throws Exception {

        PageUpdateDTO update = new PageUpdateDTO();
        update.setTitle("Test page updated");
        update.setContent("<div>Test page content updated</div>");
        update.setSlug("test-page");

        Mockito.when(pageService.updatePageBySlug(any(String.class), any(PageUpdateDTO.class))).thenThrow(new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST));

        mockMvc.perform(put("/api/v1/pages/test-page").contentType(MediaType.APPLICATION_JSON).content(asJsonString(update)))
                .andExpect(status().isBadRequest())
                .andExpect(result-> assertTrue(result.getResolvedException() instanceof ApiRequestException))
                .andExpect(result -> assertEquals("Bad request", result.getResolvedException().getMessage()))
                .andReturn()
                .getResponse()
                .getContentAsString();
    }



    @Test
    @WithUserDetails("jadmin@test.com")
    public void getPagesAuthenticated() throws Exception {

        Mockito.when(pageService.getAllPages()).thenReturn(PagesFixtures.getPagesDTOs());

        mockMvc.perform(get("/api/v1/pages"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

    }

    @Test
    @WithAnonymousUser
    public void getPagesNotAuthenticated() throws Exception {

        Mockito.when(pageService.getAllPages()).thenReturn(PagesFixtures.getPagesDTOs());

        mockMvc.perform(get("/api/v1/pages"))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();

    }

    @Test
    @WithUserDetails("jadmin@test.com")
    public void deletePageAuthenticated() throws Exception {

        Mockito.when(pageService.deletePageBySlug(any(String.class))).thenReturn(PagesFixtures.getSinglePageDTO());

        mockMvc.perform(delete("/api/v1/pages/test"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

    }

    @Test
    @WithAnonymousUser
    public void deletePageNotAuthenticated() throws Exception {

        Mockito.when(pageService.deletePageBySlug(any(String.class))).thenReturn(PagesFixtures.getSinglePageDTO());

        mockMvc.perform(delete("/api/v1/pages/test"))
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();

    }

    @Test
    @WithUserDetails("jadmin@test.com")
    public void deletePageNotFound() throws Exception {

        Mockito.when(pageService.deletePageBySlug(any(String.class))).thenThrow(new ApiRequestException("Page not found", HttpStatus.NOT_FOUND));

        mockMvc.perform(delete("/api/v1/pages/test-not-exists"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiRequestException))
                .andExpect(result -> assertEquals("Page not found", result.getResolvedException().getMessage()))
                .andReturn()
                .getResponse()
                .getContentAsString();

    }
}
