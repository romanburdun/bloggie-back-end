package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.PasswordResetDTO;
import com.bloggie.server.api.v1.models.SiteSettingsDTO;
import com.bloggie.server.domain.PasswordResetToken;
import com.bloggie.server.domain.Role;
import com.bloggie.server.domain.RoleName;
import com.bloggie.server.domain.User;
import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.repositories.PrtRepository;
import com.bloggie.server.repositories.RolesRepository;
import com.bloggie.server.repositories.UsersRepository;
import com.bloggie.server.security.jwt.JwtTokenProvider;
import com.bloggie.server.security.principals.UserPrincipal;
import com.bloggie.server.security.requests.AuthRequest;
import com.bloggie.server.security.requests.SignupRequest;
import com.bloggie.server.security.responses.AuthResponse;
import com.bloggie.server.security.responses.AuthToken;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {


    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider tokenProvider;
    private UsersRepository usersRepository;
    private RolesRepository rolesRepository;
    private SiteSettingsService siteSettingsService;
    private PrtRepository prtRepository;

    @Override
    public AuthToken register(SignupRequest request) {

        SiteSettingsDTO settingsDTO = siteSettingsService.getSettings();

        if(!settingsDTO.isRegistrationAllowed()) {
            throw new ApiRequestException("Registration is disabled", HttpStatus.METHOD_NOT_ALLOWED);
        }

        if (usersRepository.existsByEmail(request.getEmail())) {
            throw new ApiRequestException("User already exists", HttpStatus.CONFLICT);
        }

        Role role = rolesRepository.findByName(RoleName.ROLE_WRITER);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(Collections.singleton(role));


        User newUser = usersRepository.save(user);

        if (newUser == null) {
            throw new ApiRequestException("New User was not created", HttpStatus.BAD_REQUEST);
        }

        Authentication auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        ));

        SecurityContextHolder.getContext().setAuthentication(auth);

        return new AuthToken(tokenProvider.getToken(auth));
    }

    @Override
    public AuthToken login(AuthRequest request) {

        Authentication auth = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        ));

        SecurityContextHolder.getContext().setAuthentication(auth);


        return new AuthToken(tokenProvider.getToken(auth));
    }


    @Override
    public Optional<User> getRequestUser() {
        UserPrincipal user = (UserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        return usersRepository.findById(user.getId());
    }

    @Override
    public AuthResponse resetPassword(String resetToken, PasswordResetDTO resetDTO) {

        PasswordResetToken prt = prtRepository.findByToken(resetToken)
                .orElseThrow(()-> new ApiRequestException("Reset token not exists", HttpStatus.NOT_FOUND));

        if(!resetDTO.getNewPassword().equals(resetDTO.getConfirmPassword())) {
            throw new ApiRequestException("Bad request.", HttpStatus.BAD_REQUEST);
        }

        LocalDateTime expiryTime = prt.getDateCreated().plusMinutes(10);

        if(LocalDateTime.now().isBefore(expiryTime)) {
            User user = usersRepository.findByEmail(prt.getEmail())
                    .orElseThrow(()-> new ApiRequestException("User not found", HttpStatus.NOT_FOUND));

            user.setPassword(passwordEncoder.encode(resetDTO.getNewPassword()));
            usersRepository.save(user);
        }

        prtRepository.delete(prt);

        return new AuthResponse(true);
    }
}
