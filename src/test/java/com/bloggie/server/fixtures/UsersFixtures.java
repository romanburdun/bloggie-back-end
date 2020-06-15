package com.bloggie.server.fixtures;

import com.bloggie.server.domain.Role;
import com.bloggie.server.domain.RoleName;
import com.bloggie.server.domain.User;

import java.util.Collections;


public abstract class UsersFixtures {

    public static User getWriterUser() {
        Role roleOne = new Role();
        roleOne.setName(RoleName.ROLE_WRITER);
        User user = new User();
        user.setId(1L);
        user.setName("John Writer");
        user.setEmail("jwriter@test.com");
        user.setPassword("secret");
        user.setRoles(Collections.singleton(roleOne));

        return user;
    }

    public static User getAdminUser() {
        Role roleOne = new Role();
        roleOne.setName(RoleName.ROLE_ADMINISTRATOR);
        User user = new User();
        user.setId(1L);
        user.setName("John Administrator");
        user.setEmail("jadmin@test.com");
        user.setPassword("secret");
        user.setRoles(Collections.singleton(roleOne));

        return user;
    }
}
