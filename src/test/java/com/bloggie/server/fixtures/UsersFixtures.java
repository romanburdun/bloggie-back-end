package com.bloggie.server.fixtures;

import com.bloggie.server.domain.Role;
import com.bloggie.server.domain.RoleName;
import com.bloggie.server.domain.User;

import java.util.Arrays;
import java.util.HashSet;

public abstract class UsersFixtures {
    public static User getUser() {
        Role roleOne = new Role();
        roleOne.setName(RoleName.ROLE_WRITER);
        Role roleTwo = new Role();
        roleTwo.setName(RoleName.ROLE_ADMINISTRATOR);
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("jdoe@test.com");
        user.setPassword("secret");
        user.setRoles(new HashSet<>(Arrays.asList(roleOne, roleTwo)));

        return user;
    }
}
