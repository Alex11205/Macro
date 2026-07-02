package com.alex.macro.dto;

import java.time.Instant;

public record FavoriteResponse(
        String username,
        String foodName,
        Instant createdAt
) {
}
