package com.alex.macro.repository;

import com.alex.macro.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {


    public interface FavoriteFoodProjection {
        String getName();
        Double getCarb();
        Double getProtein();
        Double getFat();
        Double getCalorie();
        Long getId();
        String getImageUrl();
    }

    @Query(value = """
            SELECT f.name AS name, f.carb AS carb, f.protein AS protein, f.fat AS fat, f.calorie AS calorie, f.id AS id, f.image_url as imageUrl 
            FROM favorite fa 
            INNER JOIN food f ON fa.food_id = f.id 
            WHERE fa.user_id = :id 
            ORDER BY f.id
            """ , nativeQuery = true)
    List<FavoriteFoodProjection> findFavoriteFoodsByUserId(@Param("id") Long id);


    Optional<Favorite> findByUserIdAndFoodId(Long userId, Long foodId);

}
