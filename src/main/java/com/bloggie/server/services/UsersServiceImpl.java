package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.UserUpdateDTO;
import com.bloggie.server.domain.User;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.repositories.UsersRepository;
import com.bloggie.server.security.responses.AuthResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@AllArgsConstructor
public class UsersServiceImpl implements UsersService {

    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    @Override
    public AuthResponse updateUserProfile(UserUpdateDTO updateDTO) {

        User user = authService.getRequestUser()
                .orElseThrow(()-> new ApiRequestException("Unauthenticated request", HttpStatus.UNAUTHORIZED));

        if(!usersRepository.existsById(user.getId())) {
            throw new ApiRequestException("Unauthenticated request", HttpStatus.UNAUTHORIZED);
        }

        if(updateDTO.getCurrentPassword().equals("") && updateDTO.getCurrentPassword() == null) {
            throw new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST);
        }

        if(!passwordEncoder.matches(updateDTO.getCurrentPassword(), user.getPassword())) {
                throw new ApiRequestException("Bad request", HttpStatus.UNAUTHORIZED);
        }

        if(!updateDTO.getNewPassword().equals("") && updateDTO.getNewPassword() != null) {
            user.setPassword(passwordEncoder.encode(updateDTO.getNewPassword()));
        }

        if(!updateDTO.getName().equals("") && updateDTO.getName() != null) {
            user.setName(updateDTO.getName());
        }

        if(!updateDTO.getEmail().equals("") && updateDTO.getEmail() != null) {
            String emailRegex = "^(.+)@(.+)$";
            Pattern pattern = Pattern.compile(emailRegex);

            Matcher isValidEmail = pattern.matcher(updateDTO.getEmail());

            if(!isValidEmail.matches()) {
                throw new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST);
            }

            user.setEmail(updateDTO.getEmail());


        }


        usersRepository.save(user);

        return new AuthResponse(true);
    }
}
