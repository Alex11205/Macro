package com.alex.macro.controller;

import com.alex.macro.model.Role;
import com.alex.macro.model.User;

import com.alex.macro.service.FoodService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.alex.macro.dto.*;
import com.alex.macro.security.*;


@WebMvcTest(FoodController.class)
//@AutoConfigureMockMvc(addFilters = false)
@Import(SecurityConfig.class)
public class FoodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FoodService foodService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    //    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllFoods_ShouldFetchList_WhenAuthenticated() throws Exception {

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

        FoodResponse response = new FoodResponse(
                2L,
                "foodName",
                0.0,
                0.0,
                0.0,
                0.0,
                "url",
                "creator"
        );

        List<FoodResponse> responseList = new ArrayList<>(List.of(response));

        when(foodService.getAllFoods()).thenReturn(responseList);

        mockMvc.perform(get("/api/foods")
                        .with(user(customUserDetails)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value(response.name()))
                .andExpect(jsonPath("$[0].carb").value(response.carb()))
                .andExpect(jsonPath("$[0].protein").value(response.protein()))
                .andExpect(jsonPath("$[0].fat").value(response.fat()))
                .andExpect(jsonPath("$[0].calorie").value(response.calorie()))
                .andExpect(jsonPath("$[0].imageUrl").value(response.imageUrl()))
                .andExpect(jsonPath("$[0].createdBy").value(response.createdBy()));
    }

    @Test
    void getAllFoods_ShouldThrowUnauthorized_WhenNotAuthenticated() throws Exception {

        mockMvc.perform(get("/api/foods"))
                .andExpect(status().isUnauthorized());

    }

    @Test
//    @WithMockUser(username = "username", roles = "USER")
    void createFood_ShouldSaveFood_WhenAuthenticatedAndRequestBodyIsValid() throws Exception {

        Long id = 1L;
        String username = "creator";
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

        String name = "foodName";
        Double carb = 0.0;
        Double protein = 0.0;
        Double fat = 0.0;
        Double calorie = 0.0;
        String createdBy = "creator";

        CreateFoodRequest request = new CreateFoodRequest(
                name,
                carb,
                protein,
                fat,
                calorie,
                createdBy
        );

        CreateFoodResponse response = new CreateFoodResponse(
                name,
                carb,
                protein,
                fat,
                calorie,
                createdBy
        );

        when(foodService.createFood(any(), any())).thenReturn(response);
        mockMvc.perform(post("/api/foods")
                .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(response.name()))
                .andExpect(jsonPath("$.carb").value(response.carb()))
                .andExpect(jsonPath("$.protein").value(response.protein()))
                .andExpect(jsonPath("$.fat").value(response.fat()))
                .andExpect(jsonPath("$.calorie").value(response.calorie()))
                .andExpect(jsonPath("$.createdBy").value(response.createdBy()));
    }

    @Test
    void createFood_ShouldThrowUnauthorized_WhenNotAuthenticated() throws Exception {

        String name = "foodName";
        Double carb = 0.0;
        Double protein = 0.0;
        Double fat = 0.0;
        Double calorie = 0.0;
        String createdBy = "creator";

        CreateFoodRequest request = new CreateFoodRequest(
                name,
                carb,
                protein,
                fat,
                calorie,
                createdBy
        );

        mockMvc.perform(post("/api/foods")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
//    @WithMockUser(username = "username", roles = "USER")
    void createFood_ShouldThrowBadRequest_WhenRequestBodyNotExistOrJsonIsMalformed() throws Exception {

        Long id = 1L;
        String username = "creator";
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

        mockMvc.perform(post("/api/foods")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Malformed JSON request body or missing body."));

    }

    @Test
//    @WithMockUser(username = "username", roles = "USER")
    void createFood_ShouldReturnBadRequest_WhenValidationFails() throws Exception {

        Long id = 1L;
        String username = "creator";
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

        String name = "a".repeat(51);
        Double carb = -1.0;
        Double protein = 0.0;
        Double fat = 0.0;
        Double calorie = 0.0;
        String createdBy = "";

        CreateFoodRequest request = new CreateFoodRequest(
                name,
                carb,
                protein,
                fat,
                calorie,
                createdBy
        );

        mockMvc.perform(post("/api/foods")
                        .with(user(customUserDetails))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());


    }

}
