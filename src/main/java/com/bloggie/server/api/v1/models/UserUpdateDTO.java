package com.bloggie.server.api.v1.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserUpdateDTO {
    private String name;
    private String email;
    private String currentPassword;
    private String newPassword;

}
