package com.alex.macro.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record CreateFoodRequest(

        @Schema(example = "fish")
        @NotBlank
        @Size(max = 50, message = "Food name cannot be more than 50 characters!")
        String name,

        @Schema(example = "0.0")
        @NotNull
        @Min(value = 0, message = "Carb cannot be negative!")
        @Max(value = 1000, message = "Carb cannot exceed 1,000!")
        Double carb,


        @Schema(example = "15.0")
        @NotNull
        @Min(value = 0, message = "Protein cannot be negative!")
        @Max(value = 1000, message = "Protein cannot exceed 1,000!")
        Double protein,

        @Schema(example = "11.1")
        @NotNull
        @Min(value = 0, message = "Fat cannot be negative!")
        @Max(value = 1000, message = "Fat cannot exceed 1,000!")
        Double fat,


        @Schema(example = "99.99")
        @NotNull
        @Min(value = 0, message = "Calorie cannot be negative!")
        @Max(value = 10000, message = "Calorie cannot exceed 10,000!")
        Double calorie,

        @Schema(example = "user1")
        @NotBlank
        @Size(max = 50, message = "Creator cannot be more than 50 characters!")
        String createdBy
) {
}
