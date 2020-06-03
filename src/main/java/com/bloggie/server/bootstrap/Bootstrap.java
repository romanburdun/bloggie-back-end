package com.bloggie.server.bootstrap;

import com.bloggie.server.repositories.PostsRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
@AllArgsConstructor
public class Bootstrap implements CommandLineRunner {

    private final PostsRepository postsRepository;
    @Override
    public void run(String... args) throws Exception {
            createPosts();
    }

    public void createPosts() {
        postsRepository.saveAll(BootstrapData.getPosts());
    }
}
