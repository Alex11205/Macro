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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class FoodServiceUnitTest {

    @Mock
    FoodRepository foodRepository;

    @InjectMocks
    FoodService foodService;

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

}
