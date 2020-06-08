package com.bloggie.server.misc;

import com.bloggie.server.api.v1.models.PostDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;


import java.util.List;

@AllArgsConstructor
@Getter
public class PostsPaged {
    int totalPages;
    List<PostDTO> posts;
}
