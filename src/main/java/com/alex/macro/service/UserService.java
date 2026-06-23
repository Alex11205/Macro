package com.alex.macro.service;

import com.alex.macro.dto.LoginRequest;
import com.alex.macro.dto.LoginResponse;
import com.alex.macro.dto.RegisterRequest;
import com.alex.macro.dto.RegisterResponse;
import com.alex.macro.exceptions.NoSuchUserExistsException;
import com.alex.macro.model.User;
import com.alex.macro.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchUserExistsException("User not found with id " + id));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NoSuchUserExistsException("User not found with username " + username));
    }

//    public User createUser(User user) {
//        return userRepository.save(user);
//    }

    public RegisterResponse registerUser(RegisterRequest request) {
        // Map DTO to Entity
            String username = request.username().trim();

            if (userRepository.existsByUsername(username)) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT,
                        "Username already exists"
                );
            }

            User user = new User();
            user.setUsername(username);
            user.setEmail(request.email());

            String hashedPassword = passwordEncoder.encode(request.password());
            user.setPassword(hashedPassword);

            User savedUser = userRepository.save(user);

            // Map Entity back to safe Response DTO
            return new RegisterResponse(
                    savedUser.getId(),
                    savedUser.getUsername(),
                    savedUser.getEmail()
            );


    }

    public LoginResponse authenticate(LoginRequest loginRequest) {

        User user = getUserByUsername(loginRequest.username());

//        if (!user.getPassword().equals(loginRequest.password())) {
        if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
            String mockToken = String.valueOf(user.getId());

            return new LoginResponse(mockToken, loginRequest.username());
        }


    public User updateUser(Long userId, User updatedUser) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id " + userId));

        // Overwrite old values with new values
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());

        // Save changes back to the database
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }




}
