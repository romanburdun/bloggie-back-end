package com.bloggie.server.repositories;

import com.bloggie.server.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaRepository extends JpaRepository<Media, Long> {
    Optional<Media> findByFileName(String fileName);
}
