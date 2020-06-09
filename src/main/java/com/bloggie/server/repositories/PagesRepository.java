package com.bloggie.server.repositories;

import com.bloggie.server.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PagesRepository extends JpaRepository<Page, Long> {
    Optional<Page> findBySlug(String slug);
}
