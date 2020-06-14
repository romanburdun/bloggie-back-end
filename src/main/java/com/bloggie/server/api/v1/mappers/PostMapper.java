package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.PostDTO;
import com.bloggie.server.api.v1.models.PostExcerptDTO;
import com.bloggie.server.api.v1.models.PostUpdateDTO;
import com.bloggie.server.domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    Post postDtoToPost(PostDTO postDTO);
    PostDTO postToPostDto(Post post);
    PostExcerptDTO postToPostExcerptDto(Post post);
    Post postUpdateDtoToPost(PostUpdateDTO updateDTO);
}
