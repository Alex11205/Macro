package com.alex.macro.controller;


import com.alex.macro.dto.FavoriteFood;
import com.alex.macro.model.Food;
import com.alex.macro.service.FavoriteService;
import com.alex.macro.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favoriteList")
    public List<FavoriteFood> getFavorites(@RequestHeader("Authorization") String token) {
        Long userId = Long.parseLong(token);
        return favoriteService.getFavoritesByUser(userId);
    }

    @PostMapping("/favorites/{foodId}")
    public String addFavorite(@PathVariable Long foodId,
                              @RequestHeader("Authorization") String token) {
        Long userId = Long.parseLong(token);
        favoriteService.addFavorite(userId, foodId);
        return "UserId " + userId + " and foodId " + foodId + " has been successfully created!";
    }

    @DeleteMapping("/favorites/{foodId}")
    public String removeFavorite(@PathVariable Long foodId,
                                 @RequestHeader("Authorization") String token) {
        Long userId = Long.parseLong(token);
        favoriteService.removeFavorite(userId, foodId);
        return "UserId " + userId + " and foodId " + foodId + " has been successfully deleted!";
    }
}
