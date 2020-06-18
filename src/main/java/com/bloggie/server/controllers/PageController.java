package com.bloggie.server.controllers;

import com.bloggie.server.api.v1.models.PageDTO;
import com.bloggie.server.api.v1.models.PageUpdateDTO;
import com.bloggie.server.services.PageService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/pages")
public class PageController {

    private final PageService pageService;

    @PostMapping
    private PageDTO createPage(@RequestBody PageDTO pageDTO) {
        return pageService.createPage(pageDTO);
    }
    @PutMapping("/{slug}")
    private PageDTO updatePageBySlug(@PathVariable String slug, @RequestBody PageUpdateDTO updateDTO) {
        return pageService.updatePageBySlug(slug, updateDTO);
    }

    @GetMapping("/{slug}")
    private PageDTO getPageBySlug(@PathVariable String slug) {
        return pageService.getPageBySlug(slug);
    }

    @GetMapping
    private List<PageDTO> getAllPages() {
        return pageService.getAllPages();
    }

    @DeleteMapping("/{slug}")
    private PageDTO deletePageBySlug(@PathVariable String slug) {
        return pageService.deletePageBySlug(slug);
    }
}
