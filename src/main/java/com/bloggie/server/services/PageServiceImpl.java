package com.bloggie.server.services;

import com.bloggie.server.api.v1.mappers.CustomFieldMapper;
import com.bloggie.server.api.v1.mappers.MediaMapper;
import com.bloggie.server.api.v1.mappers.MetaMapper;
import com.bloggie.server.api.v1.mappers.PageMapper;
import com.bloggie.server.api.v1.models.*;
import com.bloggie.server.domain.CustomField;
import com.bloggie.server.domain.Media;
import com.bloggie.server.domain.Meta;
import com.bloggie.server.domain.Page;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.repositories.CustomFieldsRepository;
import com.bloggie.server.repositories.MediaRepository;
import com.bloggie.server.repositories.MetasRepository;
import com.bloggie.server.repositories.PagesRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PageServiceImpl implements PageService {

    private final PagesRepository pagesRepository;
    private final PageMapper pageMapper;
    private final MetasRepository metasRepository;
    private final MetaMapper metaMapper;
    private final CustomFieldMapper customFieldMapper;
    private final CustomFieldsRepository customFieldsRepository;
    private final MediaMapper mediaMapper;
    private final MediaRepository mediaRepository;
    @Override
    public PageDTO createPage(PageDTO pageDTO) {

        if(pageDTO.getTitle() == null || pageDTO.getContent() == null) {
            throw new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST);
        }

        if(pageDTO.getTitle().equals("") || pageDTO.getContent().equals("")) {
            throw new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST);
        }


        Meta pageMeta = metaMapper.metaDtoToMeta(pageDTO.getSeo());

        Page newPage = pageMapper.pageDtoToPage(pageDTO);
        Meta pageMetaStored = metasRepository.save(pageMeta);
        newPage.setSeo(pageMetaStored);

        if(pageDTO.getCustomFields() != null) {
            List<CustomField> fieldsUpdate = pageDTO.getCustomFields()
                    .stream()
                    .map(fieldDTO -> customFieldMapper.customFieldDtoToCustomField(fieldDTO))
                    .collect(Collectors.toList());

            List<CustomField> pageFields = new ArrayList<>();
            for (CustomField cField : fieldsUpdate) {

                if(customFieldsRepository.existsByFieldName(cField.getFieldName())) {
                    throw new ApiRequestException("Field with provided fieldName exists", HttpStatus.CONFLICT);
                }

                CustomField newField = customFieldsRepository.save(cField);
                pageFields.add(newField);
            }
            newPage.setCustomFields(pageFields);

        }

        if( pageDTO.getMedia() != null && pageDTO.getMedia().size() != 0) {

            Set<Media> pageMedia = new HashSet<>();

            for (MediaDTO mediaDTO : pageDTO.getMedia()) {
                Media foundMedia = mediaRepository.findByFileName(mediaDTO.getFileName())
                        .orElseThrow(()-> new ApiRequestException("Media data not found", HttpStatus.NOT_FOUND));
             pageMedia.add(foundMedia);
            }

            newPage.setMedia(pageMedia);
        }


        return pageMapper.pageToPageDto(pagesRepository.save(newPage));
    }

    @Override
    public PageReaderDTO getPageBySlug(String slug) {

        Page foundPage = pagesRepository.findBySlug(slug)
                .orElseThrow(()-> new ApiRequestException("Page not found", HttpStatus.NOT_FOUND));

        return pageMapper.pageToPageReaderDto(foundPage);
    }

    @Override
    public PageDTO deletePageBySlug(String slug) {
        Page foundPage = pagesRepository.findBySlug(slug)
                .orElseThrow(()-> new ApiRequestException("Page not found", HttpStatus.NOT_FOUND));

        pagesRepository.deleteById(foundPage.getId());

        return pageMapper.pageToPageDto(foundPage);

    }

    @Override
    public PageDTO updatePageBySlug(String slug, PageUpdateDTO updateDTO) {
        Page foundPage = pagesRepository.findBySlug(slug)
                .orElseThrow(()->new ApiRequestException("Page not found", HttpStatus.NOT_FOUND));


        if(!foundPage.equals(pageMapper.pageUpdateDtoToPage(updateDTO))) {
            if(updateDTO.getTitle() != null && !updateDTO.getTitle().equals(foundPage.getTitle())) {
                foundPage.setTitle(updateDTO.getTitle());
            }

            if(updateDTO.getContent() != null && !updateDTO.getContent().equals(foundPage.getContent())) {
                foundPage.setContent(updateDTO.getContent());
            }

            if(updateDTO.getSlug() != null && !updateDTO.getSlug().equals(foundPage.getSlug()) ) {
                foundPage.setSlug(updateDTO.getSlug());
            }

            if(updateDTO.getSeo() != null) {
                MetaDTO seoDTO = updateDTO.getSeo();
                Meta seo = foundPage.getSeo();

                if(seoDTO.getSeoTitle() != null && !seoDTO.getSeoTitle().equals(seo.getSeoTitle())) {
                    seo.setSeoTitle(seoDTO.getSeoTitle());
                }

                if(seoDTO.getSeoDescription() != null && !seoDTO.getSeoDescription().equals(seo.getSeoDescription())) {
                    seo.setSeoDescription(seoDTO.getSeoDescription());
                }

                foundPage.setSeo(metasRepository.save(seo));


            }

        }


        if(updateDTO.getCustomFields() != null && updateDTO.getCustomFields().size() != 0) {
            List<CustomField> fieldsUpdate = updateDTO.getCustomFields()
                    .stream()
                    .map(fieldDTO -> customFieldMapper.customFieldDtoToCustomField(fieldDTO))
                    .collect(Collectors.toList());

            for (CustomField cField : fieldsUpdate) {

                Optional<CustomField> foundCfield = customFieldsRepository.findByFieldName(cField.getFieldName());

                if(foundCfield.isPresent()) {

                    if(!foundCfield.get().equals(cField)) {
                        CustomField updatedField = foundCfield.get();
                        updatedField.setValue(cField.getValue());
                        updatedField.setType(cField.getType());

                        customFieldsRepository.save(updatedField);

                    }
                } else {
                    CustomField newField = customFieldsRepository.save(cField);
                    foundPage.getCustomFields().add(newField);
                }

            }
        }


        if( updateDTO.getMedia() != null && updateDTO.getMedia().size() != 0) {

            Set<Media> pageMedia = new HashSet<>();

            for (MediaDTO mediaDTO : updateDTO.getMedia()) {
                Media foundMedia = mediaRepository.findByFileName(mediaDTO.getFileName())
                        .orElseThrow(()-> new ApiRequestException("Media data not found", HttpStatus.NOT_FOUND));
                pageMedia.add(foundMedia);
            }

            foundPage.setMedia(pageMedia);
        }

        return pageMapper.pageToPageDto(pagesRepository.save(foundPage));
    }

    @Override
    public List<PageDTO> getAllPages() {
        return pagesRepository.findAll()
                .stream()
                .map(page-> pageMapper.pageToPageDto(page))
                .collect(Collectors.toList());
    }
}
