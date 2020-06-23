package com.bloggie.server.services;

import com.bloggie.server.security.responses.StateResponse;

import java.io.IOException;

public interface EmailService {
    StateResponse resetPasswordEmail(String email) throws IOException;
}
