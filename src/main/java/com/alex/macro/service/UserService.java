package com.alex.macro.service;

import com.alex.macro.dto.*;
import com.alex.macro.exceptions.*;
import com.alex.macro.model.Role;
import com.alex.macro.model.User;
import com.alex.macro.repository.UserRepository;
import com.alex.macro.security.CustomUserDetails;
import com.alex.macro.security.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@Service
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
//        return userRepository.findById(id)
//                .orElseThrow(() -> new NoSuchUserExistsException("User not found with id " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchUserExistsException(username));
    }

//    public User createUser(User user) {
//        return userRepository.save(user);
//    }

    public RegisterResponse registerUser(RegisterRequest request) {
        // Map DTO to Entity
            String username = request.username().trim();

            if (userRepository.existsByUsername(username)) {
                throw new UserAlreadyExistsException(username);
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(request.email());
            user.setRole(Role.USER);

            String hashedPassword = passwordEncoder.encode(request.password());
            user.setPassword(hashedPassword);

            User savedUser = userRepository.save(user);

            // Map Entity back to safe Response DTO
            return new RegisterResponse(
//                    savedUser.getId(),
                    savedUser.getUsername(),
                    savedUser.getEmail()
            );


    }

    public LoginResponse authenticate(LoginRequest loginRequest) {

        String username = loginRequest.username();
        if (!userRepository.existsByUsername(username)) {
            throw new NoSuchUserExistsException(username);
        }

//        User user = getUserByUsername(loginRequest.username());

//        if (!user.getPassword().equals(loginRequest.password())) {
//        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
//            throw new RuntimeException("Invalid password");
//        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        );

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(principal);

            return new LoginResponse(token, loginRequest.username());
        }


    public void changeEmail(ChangeEmailRequest request, String username) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchUserExistsException(username));

        if(!request.oldEmail().equals(existingUser.getEmail())) {
            throw new InvalidEmailException("The current email you entered is incorrect.");
        }


        if(request.newEmail().equals(existingUser.getEmail()))
            throw new SameEmailException("New email cannot be the same as your current email.");

        existingUser.setEmail(request.newEmail());

        userRepository.save(existingUser);
    }

    public void changePassword(ChangePasswordRequest request, String username) {
        User existingUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchUserExistsException(username));


        if (!passwordEncoder.matches(request.oldPassword(), existingUser.getPassword())) {
            throw new InvalidPasswordException("The current password you entered is incorrect.");
        }

        if (passwordEncoder.matches(request.newPassword(), existingUser.getPassword())) {
            throw new SamePasswordException("New password cannot be the same as your current password."
            );
        }

        existingUser.setPassword(passwordEncoder.encode(request.newPassword()));

        userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }




}
