package com.bloggie.server.repositories;

import com.bloggie.server.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostsRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
    void deleteBySlug(String slug);
}
