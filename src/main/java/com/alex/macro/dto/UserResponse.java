package com.alex.macro.dto;

public record UserResponse(
        Long id,
        String username,
        String email
) {}
