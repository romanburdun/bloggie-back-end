package com.bloggie.server.bootstrap;

import com.bloggie.server.domain.*;
import com.bloggie.server.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Profile("dev")
@Component
@AllArgsConstructor
public class Bootstrap implements CommandLineRunner {

    private final PostsRepository postsRepository;
    private final PagesRepository pagesRepository;
    private final RolesRepository rolesRepository;
    private final SiteSettingsRepository siteSettingsRepository;
    private final CustomFieldsRepository fieldsRepository;
    private final MetasRepository metasRepository;
    private final MediaRepository mediaRepository;
    @Override
    public void run(String... args) throws Exception {
            createPosts();
            createPages();
            createRoles();
            createSettings();
    }

    public void createPosts() {
        Post postWithMedia = BootstrapData.getPosts().get(0);
        Post postWOmedia = BootstrapData.getPosts().get(1);

        Media mediaOne = mediaRepository.save(BootstrapData.getMedia().get(0));
        mediaRepository.save(BootstrapData.getMedia().get(1));
        postWithMedia.setMedia(new HashSet<>(Arrays.asList(mediaOne)));

        postsRepository.save(postWithMedia);
        postsRepository.save(postWOmedia);
    }


    public void createPages() {

        List<Meta> seoData = metasRepository.saveAll(BootstrapData.getMeta());

        CustomField field = fieldsRepository.save(BootstrapData.getCustomField());

        List<Page> pages = BootstrapData.getPages();

        Page pageOne = pages.get(0);
        pageOne.setSeo(seoData.get(0));

        Page pageTwo = pages.get(1);
        pageTwo.setSeo(seoData.get(1));

        pagesRepository.save(pageOne);
        pagesRepository.save(pageTwo);

    }

    public void createRoles() {
        rolesRepository.saveAll(BootstrapData.getRoles());
    }

    public void createSettings() {
        siteSettingsRepository.save(BootstrapData.getSettings());
    }
}
