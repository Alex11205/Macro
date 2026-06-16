package com.alex.macro.service;

import com.alex.macro.dto.UserRequest;
import com.alex.macro.dto.UserResponse;
import com.alex.macro.exceptions.NoSuchFoodExistsException;
import com.alex.macro.exceptions.NoSuchUserExistsException;
import com.alex.macro.model.Favorite;
import com.alex.macro.model.Food;
import com.alex.macro.model.User;
import com.alex.macro.repository.FoodRepository;
import com.alex.macro.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;


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

    public UserResponse registerUser(UserRequest request) {
        // Map DTO to Entity
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());

        User savedUser = userRepository.save(user);

        // Map Entity back to safe Response DTO
        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail()
        );
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
