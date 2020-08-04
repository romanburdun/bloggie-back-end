package com.bloggie.server.services;

import com.bloggie.server.api.v1.models.SiteSettingsDTO;
import com.bloggie.server.domain.Role;
import com.bloggie.server.domain.RoleName;
import com.bloggie.server.domain.User;
import com.bloggie.server.fixtures.AuthTestConfig;
import com.bloggie.server.fixtures.UsersFixtures;
import com.bloggie.server.repositories.PrtRepository;
import com.bloggie.server.repositories.RolesRepository;
import com.bloggie.server.repositories.UsersRepository;
import com.bloggie.server.security.jwt.JwtTokenProvider;
import com.bloggie.server.security.requests.AuthRequest;
import com.bloggie.server.security.requests.SignupRequest;
import com.bloggie.server.security.responses.AuthToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider tokenProvider;
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private RolesRepository rolesRepository;
    @Mock
    private SiteSettingsService siteSettingsService;
    @Mock
    private PrtRepository prtRepository;

    private AuthService authService;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.initMocks(this);

        authService = new AuthServiceImpl(
                authenticationManager,
                passwordEncoder,
                tokenProvider,
                usersRepository,
                rolesRepository,
                siteSettingsService,
                prtRepository
        );
    }

    @Test
    void register() {
        SignupRequest request = new SignupRequest("John Doe", "jwriter@test.com", "secret");

        SiteSettingsDTO siteSettingsDTO = new SiteSettingsDTO();
        siteSettingsDTO.setRegistrationAllowed(true);
        siteSettingsDTO.setPostsPerPage(10);

        Role writerRole = new Role();
        writerRole.setName(RoleName.ROLE_WRITER);

        Authentication auth = new UsernamePasswordAuthenticationToken(UsersFixtures.getWriterUser().getEmail(), "secret");

        String dummyToken = "dummyToken";

        Mockito.when(siteSettingsService.getSettings()).thenReturn(siteSettingsDTO);
        Mockito.when(usersRepository.existsByEmail(any(String.class))).thenReturn(false);
        Mockito.when(rolesRepository.findByName(any(RoleName.class))).thenReturn(writerRole);
        Mockito.when(usersRepository.save(any(User.class))).thenReturn(UsersFixtures.getWriterUser());
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        Mockito.when(tokenProvider.getToken(any(Authentication.class))).thenReturn(dummyToken);
        AuthToken testToken = authService.register(request);

        assertNotNull(testToken);
        assertNotNull(testToken.getToken());
        assertEquals(dummyToken,testToken.getToken());

    }

    @Test
    void login() {
        User writer = UsersFixtures.getWriterUser();
        AuthRequest request = new AuthRequest(writer.getEmail(), writer.getPassword());

        Authentication auth = new UsernamePasswordAuthenticationToken(writer.getEmail(), writer.getPassword());

        String dummyToken = "dummyToken";

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        Mockito.when(tokenProvider.getToken(any(Authentication.class))).thenReturn(dummyToken);

        AuthToken testToken = authService.login(request);

        assertNotNull(testToken);
        assertNotNull(testToken.getToken());
        assertEquals(dummyToken, testToken.getToken());
    }

}