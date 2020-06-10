package com.bloggie.server.repositories;

import com.bloggie.server.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends PagingAndSortingRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
    Page<Post> findAllByDatePublishedBefore(LocalDateTime date, Pageable pageRequest);
}
