package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.UserDTO;
import com.bloggie.server.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    User userDtoToUser(UserDTO userDTO);

    UserDTO userToUserDTO(User user);
}
