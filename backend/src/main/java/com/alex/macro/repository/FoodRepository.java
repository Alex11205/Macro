package com.alex.macro.repository;

import com.alex.macro.dto.FoodResponse;
import com.alex.macro.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {
    boolean existsByName(String name);
    Optional<Food> findByName(String name);


    @Query("""
        SELECT new com.alex.macro.dto.FoodResponse(
        
            food.id,
            food.name,
            food.carb,
            food.protein,
            food.fat,
            food.calorie,
            food.imageUrl,
            food.createdBy
        )  
        FROM Food food
""")
    List<FoodResponse> findAllFoods();

    @Transactional
    @Modifying
    @Query("DELETE FROM Food u WHERE u.name = :name")
    void deleteByName(@Param("name") String name);

}
