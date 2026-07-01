package com.alex.macro.dto;

import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Value;

public record CreateFoodRequest(
        @NotBlank
        @Size(max = 50, message = "Food name cannot be more than 50 characters!")
        String name,

        @NotNull
        @Min(value = 0, message = "Carb cannot be negative!")
        @Max(value = 1000, message = "Carb cannot exceed 1,000!")
        Double carb,


        @NotNull
        @Min(value = 0, message = "Protein cannot be negative!")
        @Max(value = 1000, message = "Protein cannot exceed 1,000!")
        Double protein,


        @NotNull
        @Min(value = 0, message = "Fat cannot be negative!")
        @Max(value = 1000, message = "Fat cannot exceed 1,000!")
        Double fat,


        @NotNull
        @Min(value = 0, message = "Calorie cannot be negative!")
        @Max(value = 10000, message = "Calorie cannot exceed 10,000!")
        Double calorie,

        @NotBlank
        @Size(max = 50, message = "Creator cannot be more than 50 characters!")
        String createdBy
) {
}
