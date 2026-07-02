package com.alex.macro.dto;

public record FavoriteFood(
        String name,
        Double carb,
        Double protein,
        Double fat,
        Double calorie,
        Long id,
        String imageUrl
) {}
