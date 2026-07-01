package com.alex.macro.dto;

public record FoodResponse(

        Long id,

        String name,

        Double carb,

        Double protein,

        Double fat,

        Double calorie,

        String imageUrl,

        String createdBy
) {}
