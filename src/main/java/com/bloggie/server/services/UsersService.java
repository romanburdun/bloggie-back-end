package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.UserUpdateDTO;
import com.bloggie.server.security.responses.AuthResponse;

public interface UsersService {
    AuthResponse updateUserProfile(UserUpdateDTO updateDTO);
}
