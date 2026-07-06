package com.alex.macro.service;

import com.alex.macro.model.Favorite;
import com.alex.macro.model.Food;
import com.alex.macro.repository.FavoriteRepository;
import com.alex.macro.repository.FoodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.alex.macro.dto.*;
import com.alex.macro.exceptions.*;
import com.alex.macro.model.Role;
import com.alex.macro.model.User;
import com.alex.macro.repository.UserRepository;
import com.alex.macro.security.CustomUserDetails;
import com.alex.macro.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class FavoriteServiceUnitTest {

    @Mock
    FavoriteRepository favoriteRepository;

    @Mock
    FoodRepository foodRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    FavoriteService favoriteService;

    @Test
    void addFavorite_ShouldSaveFavorite_WhenUserAndFoodExist() {

        Long userId = 1L;
        Long foodId = 2L;
        Instant createdAt = Instant.now();
        User user = new User(
                userId,
                "username",
                "email@example.com",
                "hashed_password",
                Role.USER
        );
        Food food = new Food(
                foodId,
                "foodName",
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                "url",
                "creator"
        );
        Favorite favorite = new Favorite(user, food, createdAt);

        FavoriteResponse expectedResponse = new FavoriteResponse(
                user.getUsername(),
                food.getName(),
                Instant.now()
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);

        FavoriteResponse actualResponse = favoriteService.addFavorite(userId, foodId);
        assertEquals(expectedResponse.username(), actualResponse.username());
        assertEquals(expectedResponse.foodName(), actualResponse.foodName());

        verify(favoriteRepository, times(1)).save(any(Favorite.class));

    }

    @Test
    void addFavorite_ShouldThrowException_WhenUserNotExist() {

        Long userId = 1L;
        Long foodId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserExistsException.class, () -> {
                    favoriteService.addFavorite(userId, foodId);
        });

        verify(favoriteRepository, never()).save(any(Favorite.class));

    }

    @Test
    void addFavorite_ShouldThrowException_WhenFoodNotExist() {

        Long userId = 1L;
        Long foodId = 2L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());

        assertThrows(NoSuchFoodExistsException.class, () -> {
            favoriteService.addFavorite(userId, foodId);
        });

        verify(favoriteRepository, never()).save(any(Favorite.class));

    }

    @Test
    void removeFavorite_ShouldSaveFavorite_WhenUserAndFoodExist() {

        Long userId = 1L;
        Long foodId = 2L;
        Instant createdAt = Instant.now();
        User user = new User(
                userId,
                "username",
                "email@example.com",
                "hashed_password",
                Role.USER
        );
        Food food = new Food(
                foodId,
                "foodName",
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                "url",
                "creator"
        );

        Favorite favorite = new Favorite(
                user,
                food,
                Instant.now()
        );

        FavoriteResponse expectedResponse = new FavoriteResponse(
                user.getUsername(),
                food.getName(),
                Instant.now()
        );

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(foodRepository.findById(foodId)).thenReturn(Optional.of(food));
        when(favoriteRepository.findByUserIdAndFoodId(userId, foodId)).thenReturn(Optional.of(favorite));

//        willDoNothing().given(favoriteRepository).findByUserIdAndFoodId(userId, foodId);
        willDoNothing().given(favoriteRepository).delete(any());

        FavoriteResponse actualResponse = favoriteService.removeFavorite(userId, foodId);
        assertEquals(expectedResponse.username(), actualResponse.username());
        assertEquals(expectedResponse.foodName(), actualResponse.foodName());

        verify(favoriteRepository, times(1)).delete(any(Favorite.class));

    }

    @Test
    void removeFavorite_ShouldThrowException_WhenUserNotExist() {

        Long userId = 1L;
        Long foodId = 2L;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchUserExistsException.class, () -> {
            favoriteService.removeFavorite(userId, foodId);
        });

        verify(favoriteRepository, never()).delete(any(Favorite.class));

    }

    @Test
    void removeFavorite_ShouldThrowException_WhenFoodNotExist() {

        Long userId = 1L;
        Long foodId = 2L;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(foodRepository.findById(foodId)).thenReturn(Optional.empty());


        assertThrows(NoSuchFoodExistsException.class, () -> {
            favoriteService.removeFavorite(userId, foodId);
        });

        verify(favoriteRepository, never()).delete(any(Favorite.class));

    }
}
