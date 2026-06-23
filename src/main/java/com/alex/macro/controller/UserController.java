package com.alex.macro.controller;


import com.alex.macro.dto.LoginRequest;
import com.alex.macro.dto.LoginResponse;
import com.alex.macro.dto.RegisterRequest;
import com.alex.macro.dto.RegisterResponse;
import com.alex.macro.model.User;
import com.alex.macro.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(
                userService.getAllUsers());
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getUser(@RequestHeader("Authorization") String token) {
        Long userId = Long.parseLong(token);
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




    @PutMapping
    public ResponseEntity<User> replaceUser( @RequestHeader("Authorization") String token,
                                             @RequestBody User updatedUser) {
        Long userId = Long.parseLong(token);
        return ResponseEntity.ok(
                userService.updateUser(userId, updatedUser));
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User with id " + id + " has been successfully deleted!";
    }


}
