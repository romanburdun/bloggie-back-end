package com.bloggie.server.controllers;

import com.bloggie.server.api.v1.models.PasswordResetDTO;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.security.requests.AuthRequest;
import com.bloggie.server.security.requests.SignupRequest;
import com.bloggie.server.security.responses.AuthResponse;
import com.bloggie.server.security.responses.AuthToken;
import com.bloggie.server.services.AuthService;
import com.bloggie.server.services.EmailService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private AuthService authService;
    private EmailService emailService;

    @Autowired
    private Environment environment;

    @PostMapping("/register")
    private AuthResponse register(@RequestBody SignupRequest request, HttpServletResponse response) {

        AuthToken token = authService.register(request);

        String[] activeProfiles = environment.getActiveProfiles();
        List<String> profiles = Arrays.asList(activeProfiles);

        if (!StringUtils.hasText(token.getToken())) {
            throw new ApiRequestException("Could not obtain an access token", HttpStatus.NOT_FOUND);
        }

        Cookie cookie = new Cookie("token", token.getToken());
        if (profiles.contains("prod")) {

            cookie.setSecure(true);
        }

        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", environment.getProperty("app.host"));


        return new AuthResponse(true);
    }

    @PostMapping("/login")
    private AuthResponse login(@RequestBody AuthRequest request, HttpServletResponse response) {

        AuthToken token = authService.login(request);

        if (!StringUtils.hasText(token.getToken())) {
            throw new ApiRequestException("Could not obtain an access token", HttpStatus.NOT_FOUND);
        }


        String[] activeProfiles = environment.getActiveProfiles();
        List<String> profiles = Arrays.asList(activeProfiles);


        Cookie cookie = new Cookie("token", token.getToken());

        if (profiles.contains("prod")) {
            cookie.setSecure(true);
        }
        cookie.setHttpOnly(true);

        cookie.setMaxAge(60 * 60);
        cookie.setPath("/");
        response.addCookie(cookie);
        response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", environment.getProperty("app.host"));

        return new AuthResponse(true);
    }

    @PostMapping("/logout")
    private AuthResponse logout(HttpServletResponse response) {


        String[] activeProfiles = environment.getActiveProfiles();
        List<String> profiles = Arrays.asList(activeProfiles);


        Cookie cookie = new Cookie("token", "");

        if (profiles.contains("prod")) {
            cookie.setSecure(true);
        }
        cookie.setHttpOnly(true);

        cookie.setMaxAge(0);
        cookie.setPath("/");

        response.addCookie(cookie);
        response.addHeader("Access-Control-Expose-Headers", "Set-Cookie");
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", environment.getProperty("app.host"));

        return new AuthResponse(true);
    }

    @PostMapping("/password-reset-request/{email}")
    public AuthResponse sendResetPasswordEmail(@PathVariable String email) throws IOException {
        return emailService.resetPasswordEmail(email);
    }

    @PostMapping("/password-reset/{token}")
    public AuthResponse resetPassword(@PathVariable String token, @RequestBody PasswordResetDTO resetDTO) {
        return authService.resetPassword(token, resetDTO);
    }

}
