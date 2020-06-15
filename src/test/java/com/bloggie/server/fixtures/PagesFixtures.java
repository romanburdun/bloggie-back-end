package com.bloggie.server.fixtures;

import com.bloggie.server.api.v1.models.PageDTO;
import com.bloggie.server.domain.Page;

import java.util.Arrays;
import java.util.List;

public abstract class PagesFixtures {
    public static Page getSinglePage() {
        Page page = new Page();
        page.setTitle("Test page");
        page.setContent("<div>Test page content</div>");
        page.setSlug("test-page");

        return page;
    }

    public static PageDTO getSinglePageDTO() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTitle("Test pageDTO");
        pageDTO.setContent("<div>Test pageDTO content</div>");
        pageDTO.setSlug("test-pageDTO");

        return pageDTO;
    }

    public static Page getUpdatedPage() {
        Page page = new Page();
        page.setTitle("Test page updated");
        page.setContent("<div>Test page content updated</div>");
        page.setSlug("test-page");

        return page;
    }

    public static PageDTO getUpdatedPageDTO() {
        PageDTO pageDTO = new PageDTO();
        pageDTO.setTitle("Test pageDTO updated");
        pageDTO.setContent("<div>Test pageDTO content updated</div>");
        pageDTO.setSlug("test-pageDTO");

        return pageDTO;
    }

    public static List<Page> getPages() {
        Page blogPage = new Page();
        blogPage.setTitle("Blog page");
        blogPage.setContent("<div>Blog page content here...</div>");
        blogPage.setSlug("blog");

        Page aboutPage = new Page();
        aboutPage.setTitle("About page");
        aboutPage.setContent("<div>About page content here...</div>");
        aboutPage.setSlug("about");
        return Arrays.asList(blogPage, aboutPage);
    }

    public static List<PageDTO> getPagesDTOs() {
        PageDTO blogPage = new PageDTO();
        blogPage.setTitle("Blog page");
        blogPage.setContent("<div>Blog page content here...</div>");
        blogPage.setSlug("blog");

        PageDTO aboutPage = new PageDTO();
        aboutPage.setTitle("About page");
        aboutPage.setContent("<div>About page content here...</div>");
        aboutPage.setSlug("about");
        return Arrays.asList(blogPage, aboutPage);
    }
}
