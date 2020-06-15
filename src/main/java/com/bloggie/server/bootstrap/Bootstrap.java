package com.bloggie.server.bootstrap;

import com.bloggie.server.repositories.PagesRepository;
import com.bloggie.server.repositories.PostsRepository;
import com.bloggie.server.repositories.RolesRepository;
import com.bloggie.server.repositories.SiteSettingsRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
@AllArgsConstructor
public class Bootstrap implements CommandLineRunner {

    private final PostsRepository postsRepository;
    private final PagesRepository pagesRepository;
    private final RolesRepository rolesRepository;
    private final SiteSettingsRepository siteSettingsRepository;
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
        pagesRepository.saveAll(BootstrapData.getPages());
    }

    public void createRoles() {
        rolesRepository.saveAll(BootstrapData.getRoles());
    }

    public void createSettings() {
        siteSettingsRepository.save(BootstrapData.getSettings());
    }
}
