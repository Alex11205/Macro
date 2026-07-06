package com.alex.macro.controller;


import com.alex.macro.dto.FavoriteFood;
import com.alex.macro.dto.FavoriteResponse;
import com.alex.macro.model.Favorite;
import com.alex.macro.model.Food;
import com.alex.macro.repository.FavoriteRepository;
import com.alex.macro.security.CustomUserDetails;
import com.alex.macro.service.FavoriteService;
import com.alex.macro.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
//@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @GetMapping("/favoriteList")
    public ResponseEntity<List<FavoriteFood>> getFavorites(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(
                favoriteService.getFavoritesByUser(userId));
    }

    @PostMapping("/favorites/{foodId}")
    public ResponseEntity<FavoriteResponse> addFavorite(@PathVariable Long foodId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        FavoriteResponse favoriteResponse = favoriteService.addFavorite(userId, foodId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteResponse);
    }

    @DeleteMapping("/favorites/{foodId}")
    public ResponseEntity<FavoriteResponse> removeFavorite(@PathVariable Long foodId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        FavoriteResponse favoriteResponse = favoriteService.removeFavorite(userId, foodId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(favoriteResponse);
    }
}
