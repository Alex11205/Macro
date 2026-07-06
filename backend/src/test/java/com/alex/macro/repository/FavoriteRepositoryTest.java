package com.alex.macro.repository;

import com.alex.macro.model.Favorite;
import com.alex.macro.model.Food;
import com.alex.macro.model.User;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

public class FavoriteRepositoryTest extends BaseRepositoryTest{

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testentityManager;

    @Test
    void whenDuplicateIdPairs_ShouldThrowException() {

        String username = "username";
        String foodName = "foodName";

        User user = new User(
                username,
                "user@example.com",
                "hashed_password"
        );

        Food food = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        Favorite favorite1 = new Favorite(
                user,
                food,
                Instant.now()
        );

        Favorite favorite2 = new Favorite(
                user,
                food,
                Instant.now()
        );

        testentityManager.persistAndFlush(user);
        testentityManager.persistAndFlush(food);
        testentityManager.persistAndFlush(favorite1);

        assertThrows(DataIntegrityViolationException.class, () -> {
            favoriteRepository.save(favorite2);
        });
    }

//    @Test
//    void whenUserIsDeleted_ShouldCascadeRemoveFavorite() {
//
//        String username = "username";
//        String foodName = "foodName";
//
//        User user = new User(
//                username,
//                "user@example.com",
//                "hashed_password"
//        );
//
//        Food food = new Food(
//                foodName,
//                0.0,
//                0.0,
//                0.0,
//                0.0,
//                "creator"
//        );
//
//        Favorite favorite = new Favorite(
//                user,
//                food,
//                Instant.now()
//        );
//
//        testentityManager.persistAndFlush(user);
//        testentityManager.persistAndFlush(food);
//        testentityManager.persistAndFlush(favorite);
//
//        List<FavoriteRepository.FavoriteFoodProjection> result = favoriteRepository.findFavoriteFoodsByUserId(user.getId());
//        assertEquals(1, result.size());
//
//        foodRepository.deleteById(food.getId());
////        testentityManager.persistAndFlush(user);
////        result = favoriteRepository.findFavoriteFoodsByUserId(user.getId());
//        assertEquals(0, result.size());
//
//    }

    @Test
    void findFavoriteFoodsByUserId_ShouldReturnInterface_WhenUserIdExists() {

        String username = "username";
        String foodName1 = "foodName1";
        String foodName2 = "foodName2";

        User user = new User(
                username,
                "user@example.com",
                "hashed_password"
        );

        Food food1 = new Food(
                foodName1,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        Food food2 = new Food(
                foodName2,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        Favorite favorite1 = new Favorite(
                user,
                food1,
                Instant.now()
        );

        Favorite favorite2 = new Favorite(
                user,
                food2,
                Instant.now()
        );

        testentityManager.persistAndFlush(user);
        testentityManager.persistAndFlush(food1);
        testentityManager.persistAndFlush(food2);
        testentityManager.persistAndFlush(favorite1);
        testentityManager.persistAndFlush(favorite2);

        List<FavoriteRepository.FavoriteFoodProjection> result = favoriteRepository.findFavoriteFoodsByUserId(user.getId());

        assertEquals(2, result.size());
        assertEquals(food1.getName(), result.getFirst().getName());
        assertEquals(food1.getCarb(), result.getFirst().getCarb());
        assertEquals(food1.getProtein(), result.getFirst().getProtein());
        assertEquals(food1.getFat(), result.getFirst().getFat());
        assertEquals(food1.getCalorie(), result.getFirst().getCalorie());
        assertEquals(food1.getId(), result.getFirst().getId());
        assertEquals(food1.getImageUrl(), result.getFirst().getImageUrl());

        assertEquals(food2.getName(), result.get(1).getName());
        assertEquals(food2.getId(), result.get(1).getId());
        assertEquals(food2.getProtein(), result.get(1).getProtein());
        assertEquals(food2.getFat(), result.get(1).getFat());
        assertEquals(food2.getCalorie(), result.get(1).getCalorie());
        assertEquals(food2.getId(), result.get(1).getId());
        assertEquals(food2.getImageUrl(), result.get(1).getImageUrl());

    }

    @Test
    void findFavoriteFoodsByUserId_ShouldReturnEmpty_WhenUserIdNotExist() {

        String username = "username";
        String foodName1 = "foodName1";
        String foodName2 = "foodName2";

        User user = new User(
                username,
                "user@example.com",
                "hashed_password"
        );

        Food food1 = new Food(
                foodName1,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        Food food2 = new Food(
                foodName2,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        Favorite favorite1 = new Favorite(
                user,
                food1,
                Instant.now()
        );

        Favorite favorite2 = new Favorite(
                user,
                food2,
                Instant.now()
        );

        testentityManager.persistAndFlush(user);
        testentityManager.persistAndFlush(food1);
        testentityManager.persistAndFlush(food2);
        testentityManager.persistAndFlush(favorite1);
        testentityManager.persistAndFlush(favorite2);

        List<FavoriteRepository.FavoriteFoodProjection> result = favoriteRepository.findFavoriteFoodsByUserId(10L);

        assertEquals(0, result.size());


    }

    @Test
    void findByUserIdAndFoodId_ShouldReturnFavorite_WhenUserIdAndFoodIdExist() {

        String username = "username";
        String foodName = "foodName";

        User user = new User(
                username,
                "user@example.com",
                "hashed_password"
        );

        Food food = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        Favorite favorite = new Favorite(
                user,
                food,
                Instant.now()
        );

        testentityManager.persistAndFlush(user);
        testentityManager.persistAndFlush(food);
        Favorite savedFavorite = testentityManager.persistAndFlush(favorite);

        Optional<Favorite> result = favoriteRepository.findByUserIdAndFoodId(user.getId(), food.getId());

        assertEquals(Optional.of(savedFavorite), result);

    }

    @Test
    void findByUserIdAndFoodId_ShouldReturnEmpty_WhenUserIdOrFoodIdNotExist() {

        String username = "username";
        String foodName = "foodName";

        User user = new User(
                username,
                "user@example.com",
                "hashed_password"
        );

        Food food = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        Favorite favorite = new Favorite(
                user,
                food,
                Instant.now()
        );

        testentityManager.persistAndFlush(user);
        testentityManager.persistAndFlush(food);
        testentityManager.persistAndFlush(favorite);

        Optional<Favorite> UserIdNotExistResult = favoriteRepository.findByUserIdAndFoodId(10L, food.getId());
        Optional<Favorite> FoodIdNotExistResult = favoriteRepository.findByUserIdAndFoodId(user.getId(), 11L);
        Optional<Favorite> UserIdAndFoodIdBothNotExistResult = favoriteRepository.findByUserIdAndFoodId(10L, 11L);

        assertEquals(Optional.empty(), UserIdNotExistResult);
        assertEquals(Optional.empty(), FoodIdNotExistResult);
        assertEquals(Optional.empty(), UserIdAndFoodIdBothNotExistResult);

    }

}
