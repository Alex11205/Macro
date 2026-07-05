package com.alex.macro.repository;

import com.alex.macro.dto.FoodResponse;
import com.alex.macro.model.Food;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class FoodRepositoryTest extends BaseRepositoryTest{

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private TestEntityManager testentityManager;

    @Test
    void whenDuplicateName_ShouldThrowException() {

        String foodName = "foodName";
        Food food1 = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        Food food2 = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        testentityManager.persistAndFlush(food1);

        assertThrows(DataIntegrityViolationException.class, () -> {

            foodRepository.saveAndFlush(food2);
        });
    }

    @Test
    void whenNameIsNull_ShouldThrowException() {

        Food food = new Food(
                null,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            testentityManager.persistAndFlush(food);
        });
    }

    @Test
    void existsByName_ShouldReturnTrue_WhenFoodNameExists() {

        String foodName = "foodName";
        Food food = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );
        testentityManager.persistAndFlush(food);

        assertTrue(foodRepository.existsByName(foodName));

    }

    @Test
    void existsByName_ShouldReturnFalse_WhenFoodNameNotExist() {

        String foodName = "foodName";

        assertFalse(foodRepository.existsByName(foodName));

    }

    @Test
    void findByName_ShouldReturnFood_WhenFoodNameExists() {
        String foodName = "foodName";
        Food food = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        Food savedFood = testentityManager.persistAndFlush(food);

        assertEquals(Optional.of(savedFood), foodRepository.findByName(foodName));
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenFoodNameNotExist() {
        String foodName = "foodName";

        assertEquals(Optional.empty(), foodRepository.findByName(foodName));
    }

    @Test
    void findAllFoods_ShouldReturnList_WhenListIsNotEmpty() {

        Food food1 = new Food(
                "foodName1",
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        Food food2 = new Food(
                "foodName2",
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        testentityManager.persistAndFlush(food1);

        testentityManager.persistAndFlush(food2);


        FoodResponse foodResponse1 = new FoodResponse(
                food1.getId(),
                "foodName1",
                0.0,
                0.0,
                0.0,
                0.0,
                null,
                "creator"
        );

        FoodResponse foodResponse2 = new FoodResponse(
                food2.getId(),
                "foodName2",
                0.0,
                0.0,
                0.0,
                0.0,
                null,
                "creator"
        );

        List<FoodResponse> foodResponses = new ArrayList<>(List.of(foodResponse1, foodResponse2));

        assertEquals(foodResponses, foodRepository.findAllFoods());

    }

    @Test
    void findAllFoods_ShouldReturnEmpty_WhenListIsEmpty() {
        List<FoodResponse> foodResponses = new ArrayList<>();
        assertEquals(foodResponses, foodRepository.findAllFoods());
    }

    @Test
    void deleteByName_ShouldRemoveFood_WhenFoodNameExists() {
        String foodName = "foodName";
        Food food = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        testentityManager.persistAndFlush(food);
        assertTrue(foodRepository.existsByName(foodName));

        foodRepository.deleteByName(foodName);

        assertFalse(foodRepository.existsByName(foodName));
    }

    @Test
    void deleteByName_ShouldDoNothing_WhenFoodNameNotExist() {
        String foodName = "foodName";
        String wrongName = "wrongName";
        Food food = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "creator"
        );

        testentityManager.persistAndFlush(food);
        assertTrue(foodRepository.existsByName(foodName));

        foodRepository.deleteByName(wrongName);

        assertTrue(foodRepository.existsByName(foodName));
    }
}
