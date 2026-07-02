package com.alex.macro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangeEmailRequest(

//        @NotBlank(message = "Username cannot be empty")
//        @Size(min = 3, max = 20, message = "Username length should be between 3 and 20 characters!")
//        String username,

        @NotBlank(message = "Old Email cannot be empty")
        @Email(message = "Invalid email format")
        String oldEmail,

        @NotBlank(message = "New Email cannot be empty")
        @Email(message = "Invalid email format")
                String newEmail
) {}
