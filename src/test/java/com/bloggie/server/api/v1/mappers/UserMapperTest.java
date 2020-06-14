package com.bloggie.server.api.v1.mappers;

import com.bloggie.server.api.v1.models.UserDTO;
import com.bloggie.server.domain.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    void userDtoToUser() {

        UserDTO userDTO = new UserDTO();
        userDTO.setName("John Doe");

        User user = userMapper.userDtoToUser(userDTO);

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
    }

    @Test
    void userToUserDTO() {

        User user = new User();
        user.setName("John Doe");
        user.setEmail("je@test.test");
        user.setPassword("secret");

        UserDTO userDTO = userMapper.userToUserDTO(user);

        assertNotNull(userDTO);
        assertEquals("John Doe", userDTO.getName());
    }
}
