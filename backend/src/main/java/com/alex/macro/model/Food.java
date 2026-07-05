package com.alex.macro.model;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_food_name", columnNames = "name")
        }
)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Food name cannot be empty")
    @Column(nullable = false)
    private String name;

    private Double carb;

    private Double protein;

    private Double fat;

    private Double calorie;

    private Double weight;

    private String imageUrl;

    private String createdBy;

    public Food(String name, Double carb, Double protein, Double fat, Double calorie, String createdBy) {

        this.name = name;
        this.carb = carb;
        this.protein = protein;
        this.fat = fat;
        this.calorie = calorie;
//        this.weight = weight;
//        this.imageUrl = imageUrl;
        this.createdBy = createdBy;
    }

}
