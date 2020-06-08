package com.bloggie.server.misc;

import com.bloggie.server.api.v1.models.PostExcerptDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PostsExcerptsPaged {
    int totalPages;
    List<PostExcerptDTO> postsExcerpts;
}
