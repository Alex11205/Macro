package com.alex.macro.service;

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

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }

    public Food getFoodByName(String foodName) {
        return foodRepository.findByName(foodName)
                .orElseThrow(() -> new NoSuchFoodExistsException("Food not found with id " + foodName));
    }

    public Food createFood(Food food) {
        return foodRepository.save(food);
    }

    public Food updateFood(String foodName, Food updatedFood) {
        Food existingFood = foodRepository.findByName(foodName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Food not found"));

        // 2. Overwrite old values with new values
        existingFood.setName(updatedFood.getName());
        existingFood.setCarb(updatedFood.getCarb());
        existingFood.setProtein(updatedFood.getProtein());
        existingFood.setFat(updatedFood.getFat());
        existingFood.setCalorie(updatedFood.getCalorie());
        existingFood.setWeight(updatedFood.getWeight());
        existingFood.setImageUrl(updatedFood.getImageUrl());

        // 3. Save changes back to the database
        return foodRepository.save(existingFood);
    }

    public void deleteFood(String foodName) {
        foodRepository.deleteByName(foodName);
    }

}
