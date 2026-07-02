package com.alex.macro.controller;

import com.alex.macro.dto.*;
import com.alex.macro.model.Role;
import com.alex.macro.model.User;
import com.alex.macro.security.*;
import com.alex.macro.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@WebMvcTest(UserController.class)
//@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

//    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

//    @Mock
//    private UserRepository userRepository;

//    @MockitoBean
//    private JwtAuthenticationFilter jwtAuthenticationFilter;
//
//    @MockitoBean
//    private CustomUserDetailsService customUserDetailsService;

    @Test
    void getAllUsers_ShouldFetchList_WhenRoleIsAdmin() throws Exception {

        Long id = 1L;
        String username = "testAdmin";
        String email = "admin@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.ADMIN
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        UserAdminResponse response = new UserAdminResponse(
                1L,
                "username",
                "user@example.com",
                Role.USER
        );

        List<UserAdminResponse> responseList = new ArrayList<>(List.of(response));

        when(userService.getAllUsers()).thenReturn(responseList);

        mockMvc.perform(get("/api/users")
                .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(response.id()))
                .andExpect(jsonPath("$[0].username").value(response.username()))
                .andExpect(jsonPath("$[0].email").value(response.email()))
                .andExpect(jsonPath("$[0].role").value(response.role().name()));
    }

    @Test
    void getAllUsers_ShouldThrowForbidden_WhenRoleIsNotAdmin() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        mockMvc.perform(get("/api/users")
                        .with(user(customUserDetails)))
                .andExpect(status().isForbidden());

    }

    @Test
    void getAllUsers_ShouldThrowUnauthorized_WhenNotAuthenticated() throws Exception {

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());
    }



    @Test
    void getUser_ShouldReturnUserProfile_WhenAuthenticated() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        UserAdminResponse userAdminResponse = new UserAdminResponse(
                id,
                username,
                email,
                Role.USER);

        when(userService.getUserById(id)).thenReturn(userAdminResponse);

        mockMvc.perform(get("/api/users/profile")
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.role").value(Role.USER.name()));
    }

    @Test
    void getUser_ShouldThrowUnauthorized_WhenNotAuthenticated() throws Exception {

        mockMvc.perform(get("/api/users/profile"))
                .andExpect(status().isUnauthorized());

    }

    @Test
//    @WithMockUser(username = "username", roles = "USER")
    void createUser_ShouldSaveUser_WhenRequestBodyIsValid() throws Exception {

        String username = "testUser";
        String email = "user@example.com";
        String password = "raw_password";

        RegisterRequest request = new RegisterRequest(
                username,
                email,
                password
        );

        RegisterResponse response = new RegisterResponse(
                username,
                email
        );

        when(userService.registerUser(request)).thenReturn(response);
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email));
    }

    @Test
    void createUser_ShouldThrowBadRequest_WhenRequestBodyNotExistOrJsonIsMalformed() throws Exception {


        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request body or missing body."));


    }

//    @Test
//    void createUser_ShouldReturnBadRequest_WhenJsonIsMalformed() throws Exception {
//        String malformedJson = "{ username: 'testUser', email: }";
//
//        mockMvc.perform(post("/api/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(malformedJson))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("$.message").value("Malformed JSON request body or missing body."));
//
//    }

    @Test
    void createUser_ShouldThrowBadRequest_WhenValidationFails() throws Exception {

        String invalidPayload = """
            {
                "username": "",
                "email": "not-a-valid-email-format",
                "password": "short"
            }
            """;

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    void createUser_ShouldReturnBadRequest_WhenPasswordIsExtremelyLong() throws Exception {
//
//        String giantPassword = "a".repeat(5000);
//
//        String payloadWithGiantPassword = """
//            {
//                "username": "validUser",
//                "email": "user@example.com",
//                "password": "%s"
//            }
//            """.formatted(giantPassword);
//
//        mockMvc.perform(post("/api/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(payloadWithGiantPassword))
//                .andExpect(status().isBadRequest());
//    }

    @Test
    void login_ShouldReturnLoginResponse_WhenAuthenticationPasses() throws Exception {

        String username = "testUser";
        String password = "raw_password";
        String token = "mockJwt";

        LoginRequest request = new LoginRequest(
                username,
                password
        );

        LoginResponse response = new LoginResponse(
                token,
                username
        );

        when(userService.authenticate(request)).thenReturn(response);
        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token))
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void login_ShouldThrowBadRequest_WhenRequestBodyNotExistOrJsonIsMalformed() throws Exception {


        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request body or missing body."));


    }

    @Test
    void login_ShouldThrowBadRequest_WhenValidationFails() throws Exception {
        String malformedJson = """
            {
                "username": "",
                "password": "short"
            }
            """;

        mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(malformedJson))
                .andExpect(status().isBadRequest());
    }

//    @Test
//    void login_ShouldReturnNotFound_WhenUsernameNotExist() throws Exception {
//
//        String username = "testUser";
//        String password = "raw_password";
//        String token = "mockJwt";
//
//        LoginRequest request = new LoginRequest(
//                username,
//                password
//        );
//
//        when(userService.authenticate(request))
//                .thenThrow(new NoSuchUserExistsException(username));
//
//        mockMvc.perform(post("/api/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message").value("User '" + username + "' cannot be found."));
//
//    }

//    @Test
//    void login_ShouldReturnUnauthorized_WhenAuthenticationFails() throws Exception {
//
//        String username = "testUser";
//        String password = "raw_password";
//        String token = "mockJwt";
//
//        LoginRequest request = new LoginRequest(
//                username,
//                password
//        );
//
//        when(userService.authenticate(request))
//                .thenThrow(new BadCredentialsException("Invalid credentials"));
//
//        mockMvc.perform(post("/api/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.message").value("Invalid credentials"));
//
//    }

    @Test
    void changeEmail_ShouldReturnOk_WhenEmailIsCorrectAndUnique() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";
        String newEmail = "newEmail@example.com";
//        String oldPassword = "raw_old_password";
//        String newPassword = "raw_new_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        ChangeEmailRequest changeEmailRequest = new ChangeEmailRequest(
                email,
                newEmail
        );

        String response = "Email changed successfully!";

        willDoNothing().given(userService).changeEmail(any(), any());
//        doNothing().when(userService).changeEmail(any(), any());

        mockMvc.perform(put("/api/users/changeEmail")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeEmailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(response));

    }

    @Test
    void changeEmail_ShouldThrowUnauthorized_WhenNotAuthenticated() throws Exception {

        mockMvc.perform(put("/api/users/changeEmail"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void changeEmail_ShouldThrowForbidden_WhenRoleIsNotUser() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "admin@example.com";
        String password = "hashed_password";
        String newEmail = "newEmail@example.com";
//        String oldPassword = "raw_old_password";
//        String newPassword = "raw_new_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.ADMIN
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        ChangeEmailRequest changeEmailRequest = new ChangeEmailRequest(
                email,
                newEmail
        );


        willDoNothing().given(userService).changeEmail(any(), any());
//        doNothing().when(userService).changeEmail(any(), any());

        mockMvc.perform(put("/api/users/changeEmail")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changeEmailRequest)))
                .andExpect(status().isForbidden());
//                .andExpect(content().string(response));

    }

    @Test
    void changeEmail_ShouldThrowBadRequest_WhenRequestBodyNotExistOrJsonIsMalformed() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";
        String newEmail = "newEmail@example.com";
//        String oldPassword = "raw_old_password";
//        String newPassword = "raw_new_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);


        willDoNothing().given(userService).changeEmail(any(), any());
//        doNothing().when(userService).changeEmail(any(), any());

        mockMvc.perform(put("/api/users/changeEmail")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request body or missing body."));
    }

    @Test
    void changeEmail_ShouldThrowBadRequest_WhenValidationFails() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";
        String newEmail = "newEmail@example.com";
//        String oldPassword = "raw_old_password";
//        String newPassword = "raw_new_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);


        String invalidPayload = """
            {
                "oldEmail": "",
                "newEmail": "not-a-valid-email-format"
            }
            """;

        willDoNothing().given(userService).changeEmail(any(), any());
//        doNothing().when(userService).changeEmail(any(), any());

        mockMvc.perform(put("/api/users/changeEmail")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());

    }


    @Test
    void changePassword_ShouldReturnOk_WhenPasswordIsCorrectAndUnique() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";
        String oldPassword = "raw_old_password";
        String newPassword = "raw_new_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(
                oldPassword,
                newPassword
        );

        String response = "Password changed successfully!";

        willDoNothing().given(userService).changePassword(any(), any());
//        doNothing().when(userService).changeEmail(any(), any());

        mockMvc.perform(put("/api/users/changePassword")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(response));

    }

    @Test
    void changePassword_ShouldThrowUnauthorized_WhenNotAuthenticated() throws Exception {

        mockMvc.perform(put("/api/users/changePassword"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void changePassword_ShouldThrowForbidden_WhenRoleIsNotUser() throws Exception {

        Long id = 1L;
        String username = "testAdmin";
        String email = "admin@example.com";
        String password = "hashed_password";

        String oldPassword = "raw_old_password";
        String newPassword = "raw_new_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.ADMIN
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(
                oldPassword,
                newPassword
        );


        willDoNothing().given(userService).changeEmail(any(), any());
//        doNothing().when(userService).changeEmail(any(), any());

        mockMvc.perform(put("/api/users/changePassword")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(changePasswordRequest)))
                .andExpect(status().isForbidden());
//                .andExpect(content().string(response));

    }

    @Test
    void changePassword_ShouldThrowBadRequest_WhenRequestBodyNotExistOrJsonIsMalformed() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";


        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);


        willDoNothing().given(userService).changePassword(any(), any());
//        doNothing().when(userService).changeEmail(any(), any());

        mockMvc.perform(put("/api/users/changePassword")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request body or missing body."));
    }

    @Test
    void changePassword_ShouldThrowBadRequest_WhenValidationFails() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);


        String invalidPayload = """
            {
                "oldPassword": "",
                "newPassword": "not-a-valid-email-format"
            }
            """;

        willDoNothing().given(userService).changePassword(any(), any());
//        doNothing().when(userService).changeEmail(any(), any());

        mockMvc.perform(put("/api/users/changePassword")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest());

    }


    @Test
    void deleteUser_ShouldReturnOk_WhenRoleIsAdmin() throws Exception {

        Long id = 1L;
        String username = "testAdmin";
        String email = "admin@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.ADMIN
        );

        Long deletedId = 2L;

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        String response = "User with id " + deletedId + " has been successfully deleted!";

        willDoNothing().given(userService).deleteUser(any());
//        doNothing().when(userService).changeEmail(any(), any());

        mockMvc.perform(delete("/api/users/2")
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(content().string(response));

    }

    @Test
    void deleteUser_ShouldThrowUnauthorized_WhenNotAuthenticated() throws Exception {

        willDoNothing().given(userService).deleteUser(any());
//        doNothing().when(userService).changeEmail(any(), any());

        mockMvc.perform(delete("/api/users/2"))
                .andExpect(status().isUnauthorized());


    }

    @Test
    void deleteUser_ShouldThrowForbidden_WhenRoleIsNotAdmin() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        Long deletedId = 2L;

        CustomUserDetails customUserDetails = new CustomUserDetails(user);


        willDoNothing().given(userService).deleteUser(any());
//        doNothing().when(userService).changeEmail(any(), any());

        mockMvc.perform(delete("/api/users/2")
                        .with(user(customUserDetails)))
                .andExpect(status().isForbidden());

    }



}
