package com.alex.macro.controller;


import com.alex.macro.dto.FavoriteFoodResponse;
import com.alex.macro.dto.FavoriteResponse;
import com.alex.macro.security.CustomUserDetails;
import com.alex.macro.service.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Favorites",
        description = "Manage favorite list, add foods to list, removes from list")
@RestController
@RequestMapping("/api/favorites")
//@CrossOrigin(origins = "http://localhost:3000")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }


    @Operation(
            summary = "Fetch and display the user's favorite food list",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Favorites successfully returned"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Role is not USER")
    })
    @GetMapping("/favoriteList")
    public ResponseEntity<List<FavoriteFoodResponse>> getFavorites(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        return ResponseEntity.ok(
                favoriteService.getFavoritesByUser(userId));
    }


    @Operation(
            summary = "Add a food to favorite list",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Favorite food successfully added"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Role is not USER"),
            @ApiResponse(responseCode = "404", description = "No such user or food"),
            @ApiResponse(responseCode = "409", description = "Favorite food already exists")

    })
    @PostMapping("/favorites/{foodId}")
    public ResponseEntity<FavoriteResponse> addFavorite(@PathVariable Long foodId,
                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        FavoriteResponse favoriteResponse = favoriteService.addFavorite(userId, foodId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(favoriteResponse);
    }


    @Operation(
            summary = "Remove a food from favorite list",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Favorite food successfully removed"),
            @ApiResponse(responseCode = "401", description = "Missing or invalid JWT"),
            @ApiResponse(responseCode = "403", description = "Role is not USER"),
            @ApiResponse(responseCode = "404", description = "No such user or food")


    })
    @DeleteMapping("/favorites/{foodId}")
    public ResponseEntity<FavoriteResponse> removeFavorite(@PathVariable Long foodId,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        FavoriteResponse favoriteResponse = favoriteService.removeFavorite(userId, foodId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(favoriteResponse);
    }
}
