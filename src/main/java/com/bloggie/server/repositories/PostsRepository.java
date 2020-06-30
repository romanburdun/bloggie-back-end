package com.bloggie.server.repositories;

import com.bloggie.server.domain.Post;
import com.bloggie.server.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PostsRepository extends PagingAndSortingRepository<Post, Long> {
    Optional<Post> findBySlugAndDraftIsFalse(String slug);
    Page<Post> findAllByDatePublishedBeforeAndDraftIsFalse(LocalDateTime date, Pageable pageRequest);
    Page<Post> findAllByAuthor(User author, Pageable pageRequest);
    List<Post> findAllByTitleIgnoreCaseContaining( String searchTitle);
    List<Post> findAllByContentIgnoreCaseContaining( String searchContent);
    List<Post> findAllByMediaFileName(String fileName);


}
