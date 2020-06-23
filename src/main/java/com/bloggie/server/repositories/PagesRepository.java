package com.bloggie.server.repositories;

import com.bloggie.server.domain.Page;
import com.bloggie.server.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PagesRepository extends JpaRepository<Page, Long> {
    Optional<Page> findBySlug(String slug);
    List<Page> findAllByMediaFileName(String fileName);
}
