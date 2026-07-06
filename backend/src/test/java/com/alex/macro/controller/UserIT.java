package com.alex.macro.controller;

import com.alex.macro.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class UserIT extends BaseIT {

    @Test
    void registerLoginProfileChangeEmailShouldWork() throws Exception {

        String username = "username";
        String email = "user@example.com";
        String password = "raw_password";
        String newEmail = "new@example.com";

        RegisterRequest registerRequest = new RegisterRequest(
                username,
                email,
                password
        );

        LoginRequest loginRequest = new LoginRequest(
                username,
                password
        );

        ChangeEmailRequest changeEmailRequest =  new ChangeEmailRequest(
                email,
                newEmail
        );

        mockMvc.perform(post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").doesNotExist());


        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(username))
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token")
                .asText();

        mockMvc.perform(get("/api/users/profile")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email));

        mockMvc.perform(put("/api/users/changeEmail")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(changeEmailRequest)))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.message").value("Email changed successfully!"));

    }

    @Test
    void shouldThrowException_WhenUsernameAlreadyExists() throws Exception{
        String username1 = "username";
        String email1 = "user1@example.com";
        String password1 = "raw_password";

        RegisterRequest registerRequest1 = new RegisterRequest(
                username1,
                email1,
                password1
        );

        String username2 = "username";
        String email2 = "user2@example.com";
        String password2 = "raw_password";

        RegisterRequest registerRequest2 = new RegisterRequest(
                username2,
                email2,
                password2
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username1))
                .andExpect(jsonPath("$.email").value(email1))
                .andExpect(jsonPath("$.password").doesNotExist());

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest2)))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldThrowException_WhenAuthenticationFails() throws Exception{
        String username = "username";
        String email = "user@example.com";
        String password = "raw_password";
        String invalidPassword = "invalidPassword";

        RegisterRequest registerRequest1 = new RegisterRequest(
                username,
                email,
                password
        );

        LoginRequest loginRequest = new LoginRequest(
                username,
                invalidPassword
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").doesNotExist());

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    @Test
    void shouldThrowException_WhenRoleIsUser() throws Exception{
        String username = "username";
        String email = "user@example.com";
        String password = "raw_password";

        RegisterRequest registerRequest1 = new RegisterRequest(
                username,
                email,
                password
        );

        LoginRequest loginRequest = new LoginRequest(
                username,
                password
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest1)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").doesNotExist());

        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(username))
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token")
                .asText();

        mockMvc.perform(get("/api/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isForbidden());
    }

}

