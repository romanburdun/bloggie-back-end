package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.PageMapper;
import com.bloggie.server.api.v1.models.PageDTO;
import com.bloggie.server.api.v1.models.PageUpdateDTO;
import com.bloggie.server.domain.Page;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.repositories.PagesRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PageServiceImpl implements PageService {

    private final PagesRepository pagesRepository;
    private final PageMapper pageMapper;
    @Override
    public PageDTO createPage(PageDTO pageDTO) {

        if(pageDTO.getTitle() == null || pageDTO.getContent() == null) {
            throw new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST);
        }

        if(pageDTO.getTitle() == "" || pageDTO.getContent() == "") {
            throw new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST);
        }

        Page newPage = pageMapper.pageDtoToPage(pageDTO);

        return pageMapper.pageToPageDto(pagesRepository.save(newPage));
    }

    @Override
    public PageDTO getPageBySlug(String slug) {

        Optional<Page> foundPage = pagesRepository.findBySlug(slug);

        if(!foundPage.isPresent()) {
            throw new ApiRequestException("Page not found", HttpStatus.NOT_FOUND);
        }

        return pageMapper.pageToPageDto(foundPage.get());
    }

    @Override
    public PageDTO deletePageBySlug(String slug) {
        Optional<Page> foundPage = pagesRepository.findBySlug(slug);

        if(!foundPage.isPresent()) {
            throw new ApiRequestException("Page not found", HttpStatus.NOT_FOUND);
        }

        pagesRepository.deleteById(foundPage.get().getId());

        return pageMapper.pageToPageDto(foundPage.get());

    }

    @Override
    public PageDTO updatePageBySlug(String slug, PageUpdateDTO updateDTO) {
        Optional<Page> foundPage = pagesRepository.findBySlug(slug);

        if(!foundPage.isPresent()) {
            throw new ApiRequestException("Page not found", HttpStatus.NOT_FOUND);
        }

        Page updatePage = foundPage.get();

        if(updateDTO.getTitle() != null && !updateDTO.getTitle().equals(updatePage.getTitle())) {
            updatePage.setTitle(updateDTO.getTitle());
        }

        if(updateDTO.getContent() != null && !updateDTO.getContent().equals(updatePage.getContent())) {
            updatePage.setContent(updateDTO.getContent());
        }

        if(updateDTO.getSlug() != null && !updateDTO.getSlug().equals(updatePage.getSlug()) ) {
            updatePage.setSlug(updateDTO.getSlug());
        }

        return pageMapper.pageToPageDto(pagesRepository.save(updatePage));
    }

    @Override
    public List<PageDTO> getAllPages() {
        return pagesRepository.findAll()
                .stream()
                .map(page-> pageMapper.pageToPageDto(page))
                .collect(Collectors.toList());
    }
}
