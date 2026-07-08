package com.alex.macro.controller;


import com.alex.macro.dto.*;
import com.alex.macro.model.User;
import com.alex.macro.security.CustomUserDetails;
import com.alex.macro.service.UserService;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "Users", description = "User registration, login, profile, and account management")
@RestController
@RequestMapping("/api/users")
//@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            summary = "Admin feature",
            description = "Return a table with all existing users",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User information table successfully returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "User role is not ADMIN")
    })
    @GetMapping
    public ResponseEntity<List<UserAdminResponse>> getAllUsers() {
        return ResponseEntity.ok(
                userService.getAllUsers());
    }


    @Operation(
            summary = "Get current user profile",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profile successfully returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "404", description = "No such user")
    })
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

    @Operation(summary = "Register a new user")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing request body"),
            @ApiResponse(responseCode = "409", description = "Username already exists")
    })
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

    @Operation(summary = "User login")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User login successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing request body"),
            @ApiResponse(responseCode = "401", description = "Incorrect password"),
            @ApiResponse(responseCode = "404", description = "Username doesn't exist")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = userService.authenticate(loginRequest);
        return ResponseEntity.ok(response);



    }


    @Operation(
            summary = "Change email",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Email changed successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect old email or invalid request body"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "User role is not USER"),
            @ApiResponse(responseCode = "404", description = "No such user"),
            @ApiResponse(responseCode = "409", description = "New email is the same as current one")
    })
    @PutMapping("/changeEmail")
    public ResponseEntity<String> changeEmail(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @Valid @RequestBody ChangeEmailRequest request) {
        String username = userDetails.getUsername();
        userService.changeEmail(request, username);
        return ResponseEntity.ok("Email changed successfully!");
    }


    @Operation(
            summary = "Change password",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password changed successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect old password or invalid request body"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "User role is not USER"),
            @ApiResponse(responseCode = "404", description = "No such user"),
            @ApiResponse(responseCode = "409", description = "New password is the same as current one")
    })
    @PutMapping("/changePassword")
    public ResponseEntity<String> changePassword( @AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @Valid @RequestBody ChangePasswordRequest request) {
        String username = userDetails.getUsername();
        userService.changePassword(request, username);

        return ResponseEntity.ok("Password changed successfully!");
    }


    @Operation(
            summary = "Admin feature",
            description = "Delete existing users",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully deleted"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "User role is not ADMIN"),
            @ApiResponse(responseCode = "404", description = "User doesn't exist")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User with id " + id + " has been successfully deleted!");
    }


}
