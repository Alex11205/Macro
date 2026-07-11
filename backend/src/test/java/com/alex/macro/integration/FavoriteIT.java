package com.alex.macro.integration;

import com.alex.macro.dto.CreateFoodRequest;
import com.alex.macro.dto.LoginRequest;
import com.alex.macro.dto.RegisterRequest;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class FavoriteIT extends BaseIT {

    @Test
    void getFavoritesAddFavoritesRemoveFavoritesShouldWork() throws Exception{
        String username = "username";
        String email = "user@example.com";
        String password = "raw_password";

        String foodName = "foodName";
        Double carb = 0.0;
        Double protein = 0.0;
        Double fat = 0.0;
        Double calorie = 0.0;
        String createdBy = "username";

        RegisterRequest registerRequest = new RegisterRequest(
                username,
                email,
                password
        );

        LoginRequest loginRequest = new LoginRequest(
                username,
                password
        );

        CreateFoodRequest createFoodRequest = new CreateFoodRequest(
                foodName,
                carb,
                protein,
                fat,
                calorie
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").doesNotExist());


        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(username))
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token")
                .asText();

        mockMvc.perform(post("/api/foods")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFoodRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(foodName))
                .andExpect(jsonPath("$.carb").value(carb))
                .andExpect(jsonPath("$.protein").value(protein))
                .andExpect(jsonPath("$.fat").value(fat))
                .andExpect(jsonPath("$.calorie").value(calorie))
                .andExpect(jsonPath("$.createdBy").value(createdBy));

        MvcResult getFoodResult = mockMvc.perform(get("/api/foods")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(foodName))
                .andExpect(jsonPath("$[0].carb").value(carb))
                .andExpect(jsonPath("$[0].protein").value(protein))
                .andExpect(jsonPath("$[0].fat").value(fat))
                .andExpect(jsonPath("$[0].calorie").value(calorie))
                .andExpect(jsonPath("$[0].createdBy").value(createdBy))
                .andReturn();


        String foodIdString = JsonPath.read(getFoodResult.getResponse().getContentAsString(), "$[0].id").toString();

        String addFavoriteUrl = "/api/favorites/favorites/" + foodIdString;

        mockMvc.perform(post(addFavoriteUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.foodName").value(foodName))
                .andExpect(jsonPath("$.createdAt").exists());

        mockMvc.perform(get("/api/favorites/favoriteList")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(foodName))
                .andExpect(jsonPath("$[0].carb").value(carb))
                .andExpect(jsonPath("$[0].protein").value(protein))
                .andExpect(jsonPath("$[0].fat").value(fat))
                .andExpect(jsonPath("$[0].calorie").value(calorie))
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(delete(addFavoriteUrl)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.foodName").value(foodName))
                .andExpect(jsonPath("$.createdAt").exists());

        mockMvc.perform(get("/api/favorites/favoriteList")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void shouldThrowException_WhenDuplicateFavorite() throws Exception{
        String username = "username";
        String email = "user@example.com";
        String password = "raw_password";

        String foodName = "foodName";
        Double carb = 0.0;
        Double protein = 0.0;
        Double fat = 0.0;
        Double calorie = 0.0;
        String createdBy = "username";

        RegisterRequest registerRequest = new RegisterRequest(
                username,
                email,
                password
        );

        LoginRequest loginRequest = new LoginRequest(
                username,
                password
        );

        CreateFoodRequest createFoodRequest = new CreateFoodRequest(
                foodName,
                carb,
                protein,
                fat,
                calorie
        );

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.password").doesNotExist());


        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value(username))
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token")
                .asText();

        mockMvc.perform(post("/api/foods")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createFoodRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(foodName))
                .andExpect(jsonPath("$.carb").value(carb))
                .andExpect(jsonPath("$.protein").value(protein))
                .andExpect(jsonPath("$.fat").value(fat))
                .andExpect(jsonPath("$.calorie").value(calorie))
                .andExpect(jsonPath("$.createdBy").value(createdBy));

        MvcResult getFoodResult = mockMvc.perform(get("/api/foods")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(foodName))
                .andExpect(jsonPath("$[0].carb").value(carb))
                .andExpect(jsonPath("$[0].protein").value(protein))
                .andExpect(jsonPath("$[0].fat").value(fat))
                .andExpect(jsonPath("$[0].calorie").value(calorie))
                .andExpect(jsonPath("$[0].createdBy").value(createdBy))
                .andReturn();


        String foodIdString = JsonPath.read(getFoodResult.getResponse().getContentAsString(), "$[0].id").toString();

        String addFavoriteUrl = "/api/favorites/favorites/" + foodIdString;

        mockMvc.perform(post(addFavoriteUrl)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(username))
                .andExpect(jsonPath("$.foodName").value(foodName))
                .andExpect(jsonPath("$.createdAt").exists());

        mockMvc.perform(get("/api/favorites/favoriteList")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(foodName))
                .andExpect(jsonPath("$[0].carb").value(carb))
                .andExpect(jsonPath("$[0].protein").value(protein))
                .andExpect(jsonPath("$[0].fat").value(fat))
                .andExpect(jsonPath("$[0].calorie").value(calorie))
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(post(addFavoriteUrl)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isConflict());

        mockMvc.perform(get("/api/favorites/favoriteList")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

    }


}
