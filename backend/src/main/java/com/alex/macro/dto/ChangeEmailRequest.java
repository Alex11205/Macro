package com.alex.macro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ChangeEmailRequest(

        @Schema(example = "alex_old@example.com")
        @NotBlank(message = "Old Email cannot be empty")
        @Email(message = "Invalid email format")
        String oldEmail,

        @Schema(example = "alex_new@example.com")
        @NotBlank(message = "New Email cannot be empty")
        @Email(message = "Invalid email format")
                String newEmail
) {}
