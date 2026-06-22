package com.alex.macro.model;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_food_name", columnNames = "name")
        }
)
@Getter @Setter @NoArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private Double carb;

    private Double protein;

    private Double fat;

    private Double calorie;

    private Double weight;

    private String imageUrl;

    private String createdBy;

    public Food(String name, Double carb, Double protein, Double fat, Double calorie, Double weight, String imageUrl, String createdBy) {

        this.name = name;
        this.carb = carb;
        this.protein = protein;
        this.fat = fat;
        this.calorie = calorie;
        this.weight = weight;
        this.imageUrl = imageUrl;
        this.createdBy = createdBy;
    }

}
