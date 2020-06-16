package com.bloggie.server.controllers;

import com.bloggie.server.api.v1.models.UserUpdateDTO;
import com.bloggie.server.security.responses.AuthResponse;
import com.bloggie.server.services.UsersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private UsersService usersService;

    @PutMapping("/update")
    public AuthResponse updateUserProfile(@RequestBody UserUpdateDTO updateDTO) {
        return usersService.updateUserProfile(updateDTO);
    }
}
