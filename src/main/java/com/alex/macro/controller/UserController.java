package com.alex.macro.controller;


import com.alex.macro.dto.*;
import com.alex.macro.model.User;
import com.alex.macro.security.CustomUserDetails;
import com.alex.macro.service.UserService;
import io.jsonwebtoken.Jwt;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserAdminResponse>> getAllUsers() {
        return ResponseEntity.ok(
                userService.getAllUsers());
    }

    @GetMapping("/profile")
    public ResponseEntity<UserAdminResponse> getUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(
                userService.getUserById(userId));
    }

//    @PostMapping
//    public ResponseEntity<User> createUser(@RequestBody User user) {
//        return ResponseEntity.ok(
//                userService.createUser(user));
//    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> createUser(@Valid @RequestBody RegisterRequest request) {
        RegisterResponse response = userService.registerUser(request);
//        return new ResponseEntity<>(response, HttpStatus.CREATED);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

//    @PostMapping("/login")
//    public String login(@RequestBody LoginRequest request) {
//        User user = userService.getUserByUsername(request.username());
//
//        if (!user.getPassword().equals(request.password())) {
//            throw new RuntimeException("Invalid password");
//        }
//
//        return String.valueOf(user.getId());
//    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.authenticate(loginRequest);
        return ResponseEntity.ok(response);



    }

    @PutMapping("/changeEmail")
    public ResponseEntity<String> changeEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @Valid @RequestBody ChangeEmailRequest request) {
        String username = userDetails.getUsername();
        userService.changeEmail(request, username);
        return ResponseEntity.ok("Email changed successfully!");
    }

    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword( @AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @Valid @RequestBody ChangePasswordRequest request) {
        String username = userDetails.getUsername();
        userService.changePassword(request, username);

        return ResponseEntity.ok("Password changed successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User with id " + id + " has been successfully deleted!");
    }


}
