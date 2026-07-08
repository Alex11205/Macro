package com.alex.macro.service;

import com.alex.macro.dto.*;
import com.alex.macro.exceptions.*;
import com.alex.macro.model.Role;
import com.alex.macro.model.User;
import com.alex.macro.repository.UserRepository;
import com.alex.macro.security.CustomUserDetails;
import com.alex.macro.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;


    @Test
    void getUserById_ShouldReturnResponse_WhenIdExists() {

        Long id = 1L;
        User user = new User(id,"username", "email", "hashed_password", Role.USER);
        UserAdminResponse userAdminResponse = new UserAdminResponse(
                id,
                "username",
                "email",
                Role.USER
        );

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        assertEquals(userAdminResponse, userService.getUserById(id));

    }

    @Test
    void getUserById_ShouldThrowException_WhenIdNotExist() {

        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserExistsException.class, () -> {
            userService.getUserById(id);
        });

    }

    @Test
    void getUserByUsername_ShouldThrowException_WhenUsernameNotExist() {

        String username = "Username";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserExistsException.class, () -> {
            userService.getUserByUsername(username);
        });

    }

    @Test
    void registerUser_ShouldSaveUser_WhenUsernameIsUnique() {

        String username = "Username";
        String email = "user@example.com";
        RegisterRequest registerRequest = new RegisterRequest(username, email, "raw_password");
        RegisterResponse registerResponse = new RegisterResponse(username, email);
        User user = new User(username, email, "raw_password");

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode("raw_password")).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenReturn(user);

        assertEquals(registerResponse, userService.registerUser(registerRequest));

        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    void registerUser_ShouldThrowException_WhenUsernameIsNotUnique() {

        String username = "Username";
        String email = "user@example.com";
        RegisterRequest registerRequest = new RegisterRequest(username, email, "raw_password");
        when(userRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            userService.registerUser((registerRequest));
        });

        verify(userRepository, never()).save(any(User.class));

    }

    @Test
    void authenticate_ShouldReturnLoginResponse_WhenUsernameAndPasswordMatch() {

        LoginRequest loginRequest = new LoginRequest("Username", "raw_password");

        LoginResponse loginResponse = new LoginResponse("jwt_token", "Username");

        CustomUserDetails customUserDetails = mock(CustomUserDetails.class);

        Authentication authentication = mock(Authentication.class);


        when(userRepository.existsByUsername("Username")).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(jwtService.generateToken(customUserDetails)).thenReturn("jwt_token");

        assertEquals(loginResponse, userService.authenticate(loginRequest));

    }

    @Test
    void authenticate_ShouldThrowException_WhenUsernameNotExist() {

        LoginRequest loginRequest = new LoginRequest("Username", "raw_password");

        when(userRepository.existsByUsername("Username")).thenReturn(false);

        assertThrows(NoSuchUserExistsException.class, () -> {
            userService.authenticate(loginRequest);
        });

        verify(authenticationManager, never()).authenticate(any());
        verify(jwtService, never()).generateToken(any());

    }

    @Test
    void authenticate_ShouldThrowException_WhenPasswordIncorrect() {

        LoginRequest loginRequest = new LoginRequest("Username", "raw_password");

        when(userRepository.existsByUsername("Username")).thenReturn(true);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> {
            userService.authenticate(loginRequest);
        });

        verify(jwtService, never()).generateToken(any());

    }



    @Test
    void changeEmail_ShouldSaveUser_WhenEmailIsCorrectAndUnique() {

        String username = "exampleUser";
        User existingUser = new User(username, "old_email", "hashed_password");
        ChangeEmailRequest request = new ChangeEmailRequest("old_email", "new_email");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        userService.changeEmail(request, username);

        assertEquals("new_email", existingUser.getEmail());

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void changeEmail_ShouldThrowException_WhenUsernameNotExist() {

        String username = "exampleUser";
        User existingUser = new User(username, "user@example.com", "hashed_password");
        ChangeEmailRequest request = new ChangeEmailRequest("old_email", "new_email");

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserExistsException.class, () -> {
            userService.changeEmail(request, username);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changeEmail_ShouldThrowException_WhenOldEmailIsIncorrect() {

        String username = "exampleUser";
        User existingUser = new User(username, "old_email", "hashed_password");
        ChangeEmailRequest request = new ChangeEmailRequest("wrong_old_email", "new_email");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        assertThrows(InvalidEmailException.class, () -> {
            userService.changeEmail(request, username);
        });

        verify(userRepository, never()).save(existingUser);
    }

    @Test
    void changeEmail_ShouldThrowException_WhenSameNewEmail() {

        String username = "exampleUser";
        User existingUser = new User(username, "old_email", "hashed_password");
        ChangeEmailRequest request = new ChangeEmailRequest("old_email", "old_email");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));

        assertThrows(SameEmailException.class, () -> {
            userService.changeEmail(request, username);
        });

        verify(userRepository, never()).save(existingUser);
    }

    @Test
    void changePassword_ShouldSaveUser_WhenPasswordIsCorrectAndUnique() {

        String username = "exampleUser";
        User existingUser = new User(username, "user@example.com", "old_hashed_password");
        ChangePasswordRequest request = new ChangePasswordRequest("raw_old_password", "raw_new_password");

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("raw_old_password", "old_hashed_password")).thenReturn(true);
        when(passwordEncoder.matches("raw_new_password", "old_hashed_password")).thenReturn(false);
        when(passwordEncoder.encode("raw_new_password")).thenReturn("new_hashed_password");

        userService.changePassword(request, username);

        assertEquals("new_hashed_password", existingUser.getPassword());

        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void changePassword_ShouldThrowException_WhenUsernameNoTExist() {

        String username = "exampleUser";
        User existingUser = new User(username, "user@example.com", "old_hashed_password");
        ChangePasswordRequest request = new ChangePasswordRequest("raw_old_password", "raw_new_password");

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserExistsException.class, () -> {
            userService.changePassword(request, username);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePassword_ShouldThrowException_WhenOldPasswordIsIncorrect() {

        String username = "exampleUser";
        User existingUser = new User(username, "user@example.com", "old_hashed_password");
        ChangePasswordRequest request = new ChangePasswordRequest("raw_old_password", "raw_new_password");


        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("raw_old_password", "old_hashed_password")).thenReturn(false);

        assertThrows(InvalidPasswordException.class, () -> {
            userService.changePassword(request, username);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void changePasswor_ShouldThrowException_WhenSameNewPassword() {

        String username = "exampleUser";
        User existingUser = new User(username, "user@example.com", "old_hashed_password");
        ChangePasswordRequest request = new ChangePasswordRequest("raw_old_password", "raw_new_password");


        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("raw_old_password", "old_hashed_password")).thenReturn(true);
        when(passwordEncoder.matches("raw_new_password", "old_hashed_password")).thenReturn(true);

        assertThrows(SamePasswordException.class, () -> {
            userService.changePassword(request, username);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotExist() {
        Long id = 1L;

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserExistsException.class, () -> {
            userService.deleteUser(id);
        });

        verify(userRepository, never()).deleteById(any());
    }
}
