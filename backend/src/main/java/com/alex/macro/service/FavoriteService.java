package com.alex.macro.service;

import com.alex.macro.dto.FavoriteFood;
import com.alex.macro.dto.FavoriteResponse;
import com.alex.macro.exceptions.NoSuchFoodExistsException;
import com.alex.macro.exceptions.NoSuchUserExistsException;
import com.alex.macro.model.Favorite;
import com.alex.macro.model.Food;
import com.alex.macro.model.User;
import com.alex.macro.repository.FavoriteRepository;
import com.alex.macro.repository.FoodRepository;
import com.alex.macro.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Service
public class FavoriteService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;


//    public void deleteFood(String foodName) {
//        foodRepository.deleteByName(foodName);
//    }



//    public List<Food> getFavoritesByUser(Long userId) {
//        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
//
//        return favorites.stream()
//                .map(Favorite::getFood)
//                .toList();
//    }

    public List<FavoriteFood> getFavoritesByUser(Long userId) {
//        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
//        System.out.println("The result is: " + favorites);
//        return favorites.stream()
//                .map(Favorite::getFood)
//                .toList();
        return favoriteRepository.findFavoriteFoodsByUserId(userId);


    }

    public FavoriteResponse addFavorite(Long userId, Long foodId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserExistsException(String.valueOf(userId)));
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new NoSuchFoodExistsException(String.valueOf(foodId)));
        Instant createdAt = Instant.now();
        Favorite favorite = new Favorite(user, food, createdAt);
        favoriteRepository.save(favorite);
        return new FavoriteResponse(user.getUsername(), food.getName(), createdAt);
    }

    public FavoriteResponse removeFavorite(Long userId, Long foodId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserExistsException(String.valueOf(userId)));
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new NoSuchFoodExistsException(String.valueOf(foodId)));
        Favorite fav = favoriteRepository
                .findByUserIdAndFoodId(userId, foodId);
//                .orElseThrow();

        favoriteRepository.delete(fav);
        return new FavoriteResponse(user.getUsername(), food.getName(), fav.getCreatedAt());

    }
}
