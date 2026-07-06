package com.alex.macro.controller;

import com.alex.macro.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FoodIT extends BaseIT {

    @Test
    void getAllFoodsAndCreateFoodShouldWork() throws Exception {

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
                calorie,
                createdBy
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

        mockMvc.perform(get("/api/foods")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(foodName))
                .andExpect(jsonPath("$[0].carb").value(carb))
                .andExpect(jsonPath("$[0].protein").value(protein))
                .andExpect(jsonPath("$[0].fat").value(fat))
                .andExpect(jsonPath("$[0].calorie").value(calorie))
                .andExpect(jsonPath("$[0].createdBy").value(createdBy));

    }

    @Test
    void shouldThrowException_WhenNotAuthenticated() throws Exception{
        mockMvc.perform(get("/api/foods"))
                .andExpect(status().isUnauthorized());
    }

}
