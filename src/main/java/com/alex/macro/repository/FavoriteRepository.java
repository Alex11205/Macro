package com.alex.macro.repository;

import com.alex.macro.dto.FavoriteFood;
import com.alex.macro.model.Favorite;
import com.alex.macro.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {


//    @Query("SELECT f.name, f.carb, f.protein, f.fat, f.calorie FROM User u INNER JOIN Favorite fa ON u.id = fa.user_id INNER JOIN Food f ON fa.food = f.id")
//    @Query(value = "SELECT f.name AS name, f.carb AS carb, f.protein AS protein, f.fat AS fat, f.calorie AS calorie "
//                  +"FROM favorite fa "
//                  +"INNER JOIN food f ON fa.food_id = f.id "
//                  +"WHERE fa.user_id = :sid"
//                  , nativeQuery = true)
@Query("""
        SELECT new com.alex.macro.dto.FavoriteFood(
            food.name,
            food.carb,
            food.protein,
            food.fat,
            food.calorie,
            food.id,
            food.imageUrl
        )  
        FROM Favorite favorite 
        JOIN favorite.food food
        WHERE favorite.user.id = :id
""")
    List<FavoriteFood> findFavoriteFoodsByUserId(@Param("id") Long id);

    Favorite findByUserIdAndFoodId(Long userId, Long FoodId);

    @Transactional
    void deleteByUserIdAndFoodId(Long userId, Long FoodId);
}
