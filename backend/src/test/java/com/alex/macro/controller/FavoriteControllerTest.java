package com.alex.macro.controller;

import com.alex.macro.model.Food;
import com.alex.macro.model.Role;
import com.alex.macro.model.User;

import com.alex.macro.service.FavoriteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alex.macro.dto.*;
import com.alex.macro.security.*;


@WebMvcTest(FavoriteController.class)
@Import(SecurityConfig.class)
public class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavoriteService favoriteService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getFavorites_ShouldFetchList_WhenAuthenticatedAndRoleIsUser() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        FavoriteFoodResponse favoriteFoodResponse = new FavoriteFoodResponse(
                "foodName",
                0.0,
                0.0,
                0.0,
                0.0,
                1L,
                "url"
        );

        List<FavoriteFoodResponse> responseList = new ArrayList<>(List.of(favoriteFoodResponse));

        when(favoriteService.getFavoritesByUser(id)).thenReturn(responseList);

        mockMvc.perform(get("/api/favorites/favoriteList")
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(favoriteFoodResponse.name()))
                .andExpect(jsonPath("$[0].carb").value(favoriteFoodResponse.carb()))
                .andExpect(jsonPath("$[0].protein").value(favoriteFoodResponse.protein()))
                .andExpect(jsonPath("$[0].fat").value(favoriteFoodResponse.fat()))
                .andExpect(jsonPath("$[0].calorie").value(favoriteFoodResponse.calorie()))
                .andExpect(jsonPath("$[0].id").value(favoriteFoodResponse.id()))
                .andExpect(jsonPath("$[0].imageUrl").value(favoriteFoodResponse.imageUrl()));

    }

    @Test
    void getFavorites_ShouldThrowUnauthorized_WhenNotAuthenticated() throws Exception {

        mockMvc.perform(get("/api/favorites/favoriteList"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void getFavorites_ShouldThrowForbidden_WhenRoleIsNotUser() throws Exception {

        Long id = 1L;
        String username = "testAdmin";
        String email = "admin@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.ADMIN
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        mockMvc.perform(get("/api/favorites/favoriteList")
                        .with(user(customUserDetails)))
                .andExpect(status().isForbidden());

    }

    @Test
    void addFavorite_ShouldSaveFavorite_WhenAuthenticatedAndRoleIsUser() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Long foodId = 2L;
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

        FavoriteResponse favoriteResponse = new FavoriteResponse(
                username,
                food.getName(),
                Instant.now()
        );

        when(favoriteService.addFavorite(id, foodId)).thenReturn(favoriteResponse);

        mockMvc.perform(post("/api/favorites/favorites/2")
                        .with(user(customUserDetails)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(favoriteResponse.username()))
                .andExpect(jsonPath("$.foodName").value(favoriteResponse.foodName()));
    }

    @Test
    void addFavorite_ShouldThrowUnauthorized_WhenNotAuthenticated() throws Exception {

        mockMvc.perform(post("/api/favorites/favorites/2"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void addFavorite_ShouldThrowForbidden_WhenRoleIsNotUser() throws Exception {

        Long id = 1L;
        String username = "testAdmin";
        String email = "admin@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.ADMIN
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);


        mockMvc.perform(post("/api/favorites/favorites/2")
                        .with(user(customUserDetails)))
                .andExpect(status().isForbidden());
    }

    @Test
    void removeFavorite_ShouldReturnFavoriteResponse_WhenAuthenticatedAndRoleIsUser() throws Exception {

        Long id = 1L;
        String username = "testUser";
        String email = "user@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.USER
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        Long foodId = 2L;
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

        FavoriteResponse favoriteResponse = new FavoriteResponse(
                username,
                food.getName(),
                Instant.now()
        );

        when(favoriteService.removeFavorite(id, foodId)).thenReturn(favoriteResponse);

        mockMvc.perform(delete("/api/favorites/favorites/2")
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(favoriteResponse.username()))
                .andExpect(jsonPath("$.foodName").value(favoriteResponse.foodName()));
    }

    @Test
    void removeFavorite_ShouldThrowUnauthorized_WhenNotAuthenticated() throws Exception {

        mockMvc.perform(delete("/api/favorites/favorites/2"))
                .andExpect(status().isUnauthorized());

    }

    @Test
    void removeFavorite_ShouldThrowForbidden_WhenRoleIsNotUser() throws Exception {

        Long id = 1L;
        String username = "testAdmin";
        String email = "admin@example.com";
        String password = "hashed_password";
        User user = new User(
                id,
                username,
                email,
                password,
                Role.ADMIN
        );

        CustomUserDetails customUserDetails = new CustomUserDetails(user);


        mockMvc.perform(delete("/api/favorites/favorites/2")
                        .with(user(customUserDetails)))
                .andExpect(status().isForbidden());
    }

}
