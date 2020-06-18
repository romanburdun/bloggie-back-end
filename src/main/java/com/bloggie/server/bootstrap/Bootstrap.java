package com.bloggie.server.bootstrap;

import com.bloggie.server.domain.CustomField;
import com.bloggie.server.domain.Page;
import com.bloggie.server.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Collections;
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
    @Override
    public void run(String... args) throws Exception {
            createPosts();
            createPages();
            createRoles();
            createSettings();
    }

    public void createPosts() {
        postsRepository.saveAll(BootstrapData.getPosts());
    }


    public void createPages() {

        CustomField field = fieldsRepository.save(BootstrapData.getCustomField());

        List<Page> pages = BootstrapData.getPages();

        pages.get(1).setCustomFields(Collections.singletonList(field));

        pagesRepository.saveAll(BootstrapData.getPages());

    }

    public void createRoles() {
        rolesRepository.saveAll(BootstrapData.getRoles());
    }

    public void createSettings() {
        siteSettingsRepository.save(BootstrapData.getSettings());
    }
}
