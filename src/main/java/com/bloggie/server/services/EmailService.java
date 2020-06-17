package com.bloggie.server.services;

import com.bloggie.server.security.responses.AuthResponse;

import java.io.IOException;

public interface EmailService {
    AuthResponse resetPasswordEmail(String email) throws IOException;
}
