package com.alex.macro.repository;

import com.alex.macro.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);

    @Transactional
    void deleteByUserIdAndFoodId(Long userId, Long FoodId);
}
