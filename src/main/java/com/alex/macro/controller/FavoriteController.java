package com.alex.macro.controller;


import com.alex.macro.model.Food;
import com.alex.macro.service.FavoriteService;
import com.alex.macro.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:5173")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/{userId}/favorites")
    public List<Food> getFavorites(@PathVariable Long userId) {
        return favoriteService.getFavoritesByUser(userId);
    }

    @PostMapping("/{userId}/favorites/{foodId}")
    public String addFavorite(@PathVariable Long userId,
                            @PathVariable Long foodId) {
        favoriteService.addFavorite(userId, foodId);
        return "UserId " + userId + " and foodId " + foodId + " has been successfully created!";
    }

    @DeleteMapping("/{userId}/favorites/{foodId}")
    public String removeFavorite(@PathVariable Long userId,
                               @PathVariable Long foodId) {
        favoriteService.removeFavorite(userId, foodId);
        return "UserId " + userId + " and foodId " + foodId + " has been successfully deleted!";
    }
}
