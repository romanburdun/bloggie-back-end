package com.bloggie.server.controllers;

import com.bloggie.server.exceptions.ApiRequestException;
import com.bloggie.server.security.requests.AuthRequest;
import com.bloggie.server.security.requests.SignupRequest;
import com.bloggie.server.security.responses.AuthToken;
import com.bloggie.server.services.AuthService;
import com.bloggie.server.services.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK )
@ExtendWith(MockitoExtension.class)
class AuthControllerTest extends AsJsonController {

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private AuthService authService;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .build();
    }

    @Test
    void registerSuccess() throws Exception {

        SignupRequest request = new SignupRequest("John Doe", "jwriter@test.com", "secret");

        AuthToken resultToken = new AuthToken("dummyToken");

        Mockito.when(authService.register(any(SignupRequest.class))).thenReturn(resultToken);

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn().getResponse()
                .getContentAsString();
    }

    @Test
    void registerBadRequest() throws Exception {

        SignupRequest request = new SignupRequest("John Writer", "jwriter@test.com", "secret");


        Mockito.when(authService.register(any(SignupRequest.class))).thenThrow(new ApiRequestException("Bad request", HttpStatus.BAD_REQUEST));

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiRequestException))
                .andExpect(result-> assertEquals("Bad request", result.getResolvedException().getMessage()));
    }

    @Test
    void registerUserExists() throws Exception {

        SignupRequest request = new SignupRequest("John Writer", "jwriter@test.com", "secret");


        Mockito.when(authService.register(any(SignupRequest.class))).thenThrow(new ApiRequestException("User already exists", HttpStatus.CONFLICT));

        mockMvc.perform(post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isConflict())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiRequestException))
                .andExpect(result-> assertEquals("User already exists", result.getResolvedException().getMessage()));
    }

    @Test
    void loginSuccess() throws Exception {

        AuthRequest request = new AuthRequest("jwriter@test.com", "secret");
        AuthToken resultToken = new AuthToken("dummyToken");

        Mockito.when(authService.login(any(AuthRequest.class))).thenReturn(resultToken);

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();



    }

    @Test
    void loginFail() throws Exception {

        AuthRequest request = new AuthRequest("jwriter@test.com", "secret");

        Mockito.when(authService.login(any(AuthRequest.class))).thenThrow(new ApiRequestException("Could not obtain an access token", HttpStatus.NOT_FOUND));

        mockMvc.perform(post("/api/v1/auth/login").contentType(MediaType.APPLICATION_JSON).content(asJsonString(request)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ApiRequestException))
                .andExpect(result -> assertEquals("Could not obtain an access token", result.getResolvedException().getMessage()));



    }
}