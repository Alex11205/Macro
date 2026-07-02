package com.alex.macro.dto;

import com.alex.macro.model.Role;

public record UserAdminResponse(
        Long id,
        String username,
        String email,
        Role role
) {}
