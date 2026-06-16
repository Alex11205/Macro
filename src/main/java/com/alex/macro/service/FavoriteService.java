package com.alex.macro.service;

import com.alex.macro.exceptions.NoSuchFoodExistsException;
import com.alex.macro.model.Favorite;
import com.alex.macro.model.Food;
import com.alex.macro.model.User;
import com.alex.macro.repository.FavoriteRepository;
import com.alex.macro.repository.FoodRepository;
import com.alex.macro.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FavoriteService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;


    public void deleteFood(String foodName) {
        foodRepository.deleteByName(foodName);
    }

    public Favorite addFavorite(Long userId, Long foodId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchFoodExistsException("User id not found: " + userId));
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new NoSuchFoodExistsException("Food id not found: " + foodId));

        Favorite favorite = new Favorite(user, food);
        return favoriteRepository.save(favorite);
    }

    public List<Food> getFavoritesByUser(Long userId) {
        List<Favorite> favorites = favoriteRepository.findByUserId(userId);

        return favorites.stream()
                .map(Favorite::getFood)
                .toList();
    }

    public void removeFavorite(Long userId, Long foodId) {
        favoriteRepository.deleteByUserIdAndFoodId(userId, foodId);
    }
}
