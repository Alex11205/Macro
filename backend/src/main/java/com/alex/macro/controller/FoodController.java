package com.alex.macro.controller;


import com.alex.macro.dto.CreateFoodRequest;
import com.alex.macro.dto.CreateFoodResponse;
import com.alex.macro.dto.FoodResponse;
import com.alex.macro.model.Food;
import com.alex.macro.security.CustomUserDetails;
import com.alex.macro.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Foods", description = "View all existing foods and create custom food")
@RestController
@RequestMapping("/api/foods")
//@CrossOrigin(origins = "http://localhost:3000")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }


    @Operation(
            summary = "Fetch and display all existing foods",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "All foods successfully returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
    })
    @GetMapping
    public ResponseEntity<List<FoodResponse>> getAllFoods() {
        return ResponseEntity.ok(
                foodService.getAllFoods());
    }


    @Operation(
            summary = "Create custom foods",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Food created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid or missing request body"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "409", description = "Food name already exists"),
    })
    @PostMapping
    public ResponseEntity<CreateFoodResponse> createFood(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                         @Valid @RequestBody CreateFoodRequest request) {
        CreateFoodResponse response = foodService.createFood(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED)
                        .body(response);
    }
    //   ---Unused feature---

    //    @GetMapping("/{foodName}")
//    public ResponseEntity<Food> getFood(@PathVariable String foodName) {
//        return ResponseEntity.ok(
//                foodService.getFoodByName(foodName));
//    }
//
//    @PutMapping("/{foodName}")
//    public ResponseEntity<Food> replaceFood( @PathVariable String foodName, @RequestBody Food updatedFood) {
//
//        return ResponseEntity.ok(
//                foodService.updateFood(foodName, updatedFood));
//
//    }
//
//    @DeleteMapping("/{foodName}")
//    public ResponseEntity<String> deleteFood(@PathVariable String foodName) {
//        foodService.deleteFood(foodName);
//        return ResponseEntity.ok("Food with name " + foodName + " has been successfully deleted!");
//    }


}
