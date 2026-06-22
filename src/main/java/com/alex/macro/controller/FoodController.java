package com.alex.macro.controller;


import com.alex.macro.model.Food;
import com.alex.macro.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/foods")
@CrossOrigin(origins = "http://localhost:3000")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public ResponseEntity<List<Food>> getAllFoods() {
        return ResponseEntity.ok(
                foodService.getAllFoods());
    }

    @GetMapping("/{foodName}")
    public ResponseEntity<Food> getFood(@PathVariable String foodName) {
        return ResponseEntity.ok(
                foodService.getFoodByName(foodName));
    }

    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody Food food) {
        return ResponseEntity.ok(
                foodService.createFood(food));
    }

    @PutMapping("/{foodName}")
    public ResponseEntity<Food> replaceFood( @PathVariable String foodName, @RequestBody Food updatedFood) {

        return ResponseEntity.ok(
                foodService.updateFood(foodName, updatedFood));

    }

    @DeleteMapping("/{foodName}")
    public String deleteFood(@PathVariable String foodName) {
        foodService.deleteFood(foodName);
        return "Food with name " + foodName + " has been successfully deleted!";
    }


}
