package com.alex.macro.service;

import com.alex.macro.dto.*;
import com.alex.macro.exceptions.*;
import com.alex.macro.model.Role;
import com.alex.macro.model.User;
import com.alex.macro.repository.UserRepository;
import com.alex.macro.security.CustomUserDetails;
import com.alex.macro.security.JwtService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;


    public List<UserAdminResponse> getAllUsers() {
        return userRepository.findAllUsers();
    }

    public UserAdminResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserExistsException(String.valueOf(id)));
        return new UserAdminResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchUserExistsException(username));
    }

    public RegisterResponse registerUser(RegisterRequest request) {
        // Map DTO to Entity
            String username = request.username().trim();

            if (userRepository.existsByUsername(username)) {
                log.warn("Registration was rejected because username already exists: username={}", username);
                throw new UserAlreadyExistsException(username);
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(request.email());
            user.setRole(Role.USER);

            String hashedPassword = passwordEncoder.encode(request.password());
            user.setPassword(hashedPassword);

            User savedUser = userRepository.save(user);

            log.info("Register successfully: username={}", username);

            return new RegisterResponse(
                    savedUser.getUsername(),
                    savedUser.getEmail()
            );


    }

    public LoginResponse authenticate(LoginRequest loginRequest) {

        String username = loginRequest.username();
        if (!userRepository.existsByUsername(username)) {
            throw new NoSuchUserExistsException(username);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(principal);

        log.info("User logged in successfully: username={}", username);

            return new LoginResponse(token, loginRequest.username());
        }


    public void changeEmail(ChangeEmailRequest request, String username) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchUserExistsException(username));

        if(!request.oldEmail().equals(existingUser.getEmail())) {
            log.warn("Failed changing email: username={}", username);
            throw new InvalidEmailException("The current email you entered is incorrect.");
        }


        if(request.newEmail().equals(existingUser.getEmail())) {
            log.warn("Failed changing email: username={}", username);
            throw new SameEmailException("New email cannot be the same as your current email.");
        }


        existingUser.setEmail(request.newEmail());

        log.info("Email changed successfully: username={}", username);

        userRepository.save(existingUser);
    }

    public void changePassword(ChangePasswordRequest request, String username) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchUserExistsException(username));


        if (!passwordEncoder.matches(request.oldPassword(), existingUser.getPassword())) {
            log.warn("Failed changing password: username={}", username);
            throw new InvalidPasswordException("The current password you entered is incorrect.");
        }

        if (passwordEncoder.matches(request.newPassword(), existingUser.getPassword())) {
            log.warn("Failed changing password: username={}", username);
            throw new SamePasswordException("New password cannot be the same as your current password."
            );
        }

        existingUser.setPassword(passwordEncoder.encode(request.newPassword()));

        log.info("Password changed successfully: username={}", username);

        userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserExistsException(String.valueOf(id)));

        String username = user.getUsername();

        log.info("User deleted successfully: username={}", username);
        userRepository.deleteById(id);
    }


}
