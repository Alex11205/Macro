package com.alex.macro.service;

import com.alex.macro.dto.FavoriteFoodResponse;
import com.alex.macro.dto.FavoriteResponse;
import com.alex.macro.exceptions.FavoriteAlreadyExistsException;
import com.alex.macro.exceptions.NoSuchFoodExistsException;
import com.alex.macro.exceptions.NoSuchUserExistsException;
import com.alex.macro.model.Favorite;
import com.alex.macro.model.Food;
import com.alex.macro.model.User;
import com.alex.macro.repository.FavoriteRepository;
import com.alex.macro.repository.FoodRepository;
import com.alex.macro.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@AllArgsConstructor
@Service
@Slf4j
public class FavoriteService {

    private final FoodRepository foodRepository;
    private final UserRepository userRepository;
    private final FavoriteRepository favoriteRepository;


    public List<FavoriteFoodResponse> getFavoritesByUser(Long userId) {
//        List<Favorite> favorites = favoriteRepository.findByUserId(userId);
//        System.out.println("The result is: " + favorites);
//        return favorites.stream()
//                .map(Favorite::getFood)
//                .toList();
        return favoriteRepository.findFavoriteFoodsByUserId(userId)
                .stream()
                .map(this::toFavoriteFood)
                .toList();

//        return favoriteRepository.findFavoriteFoodsByUserId(userId);


    }

    private FavoriteFoodResponse toFavoriteFood(FavoriteRepository.FavoriteFoodProjection projection) {
        return new FavoriteFoodResponse(
                projection.getName(),
                projection.getCarb(),
                projection.getProtein(),
                projection.getFat(),
                projection.getCalorie(),
                projection.getId(),
                projection.getImageUrl()
        );
    }

    public FavoriteResponse addFavorite(Long userId, Long foodId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserExistsException(String.valueOf(userId)));
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new NoSuchFoodExistsException(String.valueOf(foodId)));
        Instant createdAt = Instant.now();
        Favorite favorite = new Favorite(user, food, createdAt);
        if (favoriteRepository.findByUserIdAndFoodId(userId, foodId).isPresent()) {
            log.warn("Duplicate favorite rejected: userId={}, foodId={}", userId, foodId);
            throw new FavoriteAlreadyExistsException("This favorite Item already exists!");
        }
        favoriteRepository.save(favorite);
        log.info("Favorite added: userId={}, foodId={}", userId, foodId);
        return new FavoriteResponse(user.getUsername(), food.getName(), createdAt);
    }

    public FavoriteResponse removeFavorite(Long userId, Long foodId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchUserExistsException(String.valueOf(userId)));
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new NoSuchFoodExistsException(String.valueOf(foodId)));
        Favorite fav = favoriteRepository
                .findByUserIdAndFoodId(userId, foodId)
                .orElseThrow(() -> new NoSuchFoodExistsException(String.valueOf(foodId)));

        favoriteRepository.delete(fav);
        log.info("Favorite deleted: userId={}, foodId={}", userId, foodId);
        return new FavoriteResponse(user.getUsername(), food.getName(), fav.getCreatedAt());

    }
}
