package com.alex.macro.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_favorite_user_food",
                columnNames = {"user_id", "food_id"}
        )
})
@Getter @Setter @NoArgsConstructor
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    private Instant createdAt;

    public Favorite(User user, Food food, Instant createdAt) {
        this.user = user;
        this.food = food;
        this.createdAt = createdAt;
    }
}
