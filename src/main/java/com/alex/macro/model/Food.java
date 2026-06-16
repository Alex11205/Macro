package com.alex.macro.model;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter @NoArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double carb;

    private Double protein;

    private Double fat;

    private Double calorie;

    private Double weight;

    private String imageUrl;

    public Food(String name, Double carb, Double protein, Double fat, Double calorie, Double weight, String imageUrl) {

        this.name = name;
        this.carb = carb;
        this.protein = protein;
        this.fat = fat;
        this.calorie = calorie;
        this.weight = weight;
        this.imageUrl = imageUrl;
    }

}
