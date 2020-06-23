package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.UserUpdateDTO;
import com.bloggie.server.security.responses.StateResponse;

public interface UsersService {
    StateResponse updateUserProfile(UserUpdateDTO updateDTO);
}
