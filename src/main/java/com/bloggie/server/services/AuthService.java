package com.bloggie.server.services;

import com.bloggie.server.security.requests.AuthRequest;
import com.bloggie.server.security.requests.SignupRequest;
import com.bloggie.server.security.responses.AuthToken;

public interface AuthService {
    AuthToken register(SignupRequest request);
    AuthToken login(AuthRequest request);
}
