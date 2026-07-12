package com.alex.macro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Schema(example = "alex123")
        @NotBlank(message = "Username cannot be empty")
        @Size(min = 3, max = 20, message = "Username length should be between 3 and 20 characters!")
        String username,

        @Schema(example = "alex@example.com")
        @NotBlank(message = "Email cannot be empty")
        @Email(message = "Invalid email format")
        String email,

        @Schema(example = "password123")
        @NotBlank(message = "Password cannot be empty")
        @Size(min = 8, max = 20, message = "Password length should be between 8 and 20 characters!")
        String password
) {}
