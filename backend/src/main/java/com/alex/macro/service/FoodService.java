package com.alex.macro.service;

import com.alex.macro.dto.CreateFoodRequest;
import com.alex.macro.dto.CreateFoodResponse;
import com.alex.macro.dto.FoodResponse;
import com.alex.macro.exceptions.FoodAlreadyExistsException;
import com.alex.macro.exceptions.NoSuchFoodExistsException;
import com.alex.macro.model.Food;
import com.alex.macro.repository.FoodRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FoodService {
    private final FoodRepository foodRepository;


    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<FoodResponse> getAllFoods() {
        return foodRepository.findAllFoods();
    }



    public CreateFoodResponse createFood(CreateFoodRequest request, String username) {
        String name = request.name().trim();
        if (foodRepository.existsByName(name)) {
            throw new FoodAlreadyExistsException(name);
        }

        Food food = new Food(
                request.name(),
                request.carb(),
                request.protein(),
                request.fat(),
                request.calorie(),
                username
        );

        Food savedFood = foodRepository.save(food);

        return new CreateFoodResponse(
                savedFood.getName(),
                savedFood.getCarb(),
                savedFood.getProtein(),
                savedFood.getFat(),
                savedFood.getCalorie(),
                savedFood.getCreatedBy()
        );
    }


//   ---Unused feature---
    //    public Food getFoodByName(String foodName) {
//        return foodRepository.findByName(foodName)
//                .orElseThrow(() -> new NoSuchFoodExistsException(foodName));
//    }

//    public Food updateFood(String foodName, Food updatedFood) {
//        Food existingFood = foodRepository.findByName(foodName)
//                .orElseThrow(() -> new NoSuchFoodExistsException(foodName));
//
//        // Overwrite old values with new values
//        existingFood.setName(updatedFood.getName());
//        existingFood.setCarb(updatedFood.getCarb());
//        existingFood.setProtein(updatedFood.getProtein());
//        existingFood.setFat(updatedFood.getFat());
//        existingFood.setCalorie(updatedFood.getCalorie());
//        existingFood.setWeight(updatedFood.getWeight());
//        existingFood.setImageUrl(updatedFood.getImageUrl());
//
//        // Save changes back to the database
//        return foodRepository.save(existingFood);
//    }
//
//    public void deleteFood(String foodName) {
//        if (!foodRepository.existsByName(foodName)) {
//            throw new NoSuchFoodExistsException(foodName);
//        }
//        foodRepository.deleteByName(foodName);
//    }


}
