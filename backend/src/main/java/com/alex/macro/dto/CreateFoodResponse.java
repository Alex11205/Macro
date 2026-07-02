package com.alex.macro.dto;

public record CreateFoodResponse(
        String name,
        Double carb,
        Double protein,
        Double fat,
        Double calorie,
        String createdBy
) {
}
