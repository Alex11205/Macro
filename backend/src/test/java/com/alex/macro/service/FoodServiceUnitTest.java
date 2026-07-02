package com.alex.macro.service;

import com.alex.macro.model.Food;
import com.alex.macro.repository.FoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.alex.macro.dto.*;
import com.alex.macro.exceptions.*;

import org.mockito.InjectMocks;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class FoodServiceUnitTest {

    @Mock
    FoodRepository foodRepository;

    @InjectMocks
    FoodService foodService;

    @Test
    void getFoodByName_ShouldThrowException_WhenFoodNameNotExist() {

        String foodName = "foodName";

        when(foodRepository.findByName(foodName)).thenReturn(Optional.empty());
        assertThrows(NoSuchFoodExistsException.class, () -> {
            foodService.getFoodByName(foodName);
        });

//        verify(foodRepository, never()).findByName(any());
    }

    @Test
    void createFood_ShouldSaveFood_WhenFoodNameIsUnique() {

        String foodName = "foodName";

        Food food = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "username"
        );

        CreateFoodRequest request = new CreateFoodRequest(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                ""
        );

        CreateFoodResponse response = new CreateFoodResponse(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "username"
        );

        when(foodRepository.existsByName(foodName)).thenReturn(false);
        when(foodRepository.save(any(Food.class))).thenReturn(food);

        assertEquals(response, foodService.createFood(request, "username"));

        verify(foodRepository, times(1)).save(any(Food.class));
    }

    @Test
    void createFood_ShouldThrowException_WhenFoodNameExists() {

        String foodName = "foodName";

        Food food = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "username"
        );

        CreateFoodRequest request = new CreateFoodRequest(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                ""
        );

        when(foodRepository.existsByName(foodName)).thenReturn(true);


        assertThrows(FoodAlreadyExistsException.class, () ->  {
            foodService.createFood(request, "username");
        });

        verify(foodRepository, never()).save(any(Food.class));
    }

    @Test
    void updateFood_ShouldSaveFood_WhenFoodExists() {
        String foodName = "foodName";

        Food existingFood = new Food(
                foodName,
                0.0,
                0.0,
                0.0,
                0.0,
                "username"
        );

        Food updatedFood = new Food(
                foodName,
                1.0,
                1.0,
                1.0,
                1.0,
                "username"
        );

        when(foodRepository.findByName(foodName)).thenReturn(Optional.of(existingFood));
        when(foodRepository.save( existingFood)).thenReturn(existingFood);

        assertEquals(existingFood, foodService.updateFood(foodName, updatedFood));

        verify(foodRepository, times(1)).save(any(Food.class));

    }

    @Test
    void updateFood_ShouldThrowException_WhenFoodNotExist() {
        String foodName = "foodName";

        Food updatedFood = new Food(
                foodName,
                1.0,
                1.0,
                1.0,
                1.0,
                "username"
        );

        when(foodRepository.findByName(foodName)).thenReturn(Optional.empty());

        assertThrows(NoSuchFoodExistsException.class, () -> {
            foodService.updateFood(foodName, updatedFood);
        });

        verify(foodRepository, never()).save(any(Food.class));
    }
}
