package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.PasswordResetDTO;
import com.bloggie.server.domain.User;
import com.bloggie.server.security.requests.AuthRequest;
import com.bloggie.server.security.requests.SignupRequest;
import com.bloggie.server.security.responses.StateResponse;
import com.bloggie.server.security.responses.AuthToken;

import java.util.Optional;

public interface AuthService {
    AuthToken register(SignupRequest request);
    AuthToken login(AuthRequest request);
    Optional<User> getRequestUser();
    StateResponse resetPassword(String resetToken, PasswordResetDTO resetDTO);
}
