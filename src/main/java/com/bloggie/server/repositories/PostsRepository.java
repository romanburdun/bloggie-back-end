package com.bloggie.server.repositories;

import com.bloggie.server.domain.Post;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostsRepository extends PagingAndSortingRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
}
