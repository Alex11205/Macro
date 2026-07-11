package com.alex.macro.e2e;

import com.alex.macro.dto.*;
import com.alex.macro.exceptions.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestRestTemplate
@ActiveProfiles("test")
@Sql(
        statements = {
                "TRUNCATE TABLE favorite RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE food RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE users RESTART IDENTITY CASCADE"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public class UserE2EIT {

    @ServiceConnection
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        postgres.start();
    }

    @Autowired
    TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;


    @Test
    void registerLoginProfileChangeEmailShouldWork()  {

        String username = "username";
        String email = "user@example.com";
        String password = "raw_password";
        String newPassword = "new_raw_password";

        RegisterRequest registerRequest = new RegisterRequest(
                username,
                email,
                password
        );

        LoginRequest loginRequest = new LoginRequest(
                username,
                password
        );

        ChangePasswordRequest changePasswordRequest =  new ChangePasswordRequest(
                password,
                newPassword
        );

        String foodName = "foodName";
        Double carb = 0.0;
        Double protein = 0.0;
        Double fat = 0.0;
        Double calorie = 0.0;
        String createdBy = "username";

        CreateFoodRequest createFoodRequest = new CreateFoodRequest(
                foodName,
                carb,
                protein,
                fat,
                calorie
        );

        ResponseEntity<RegisterResponse> createUser = restTemplate.postForEntity("http://localhost:" + port + "/api/users/register", registerRequest, RegisterResponse.class);

        assertThat(createUser.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createUser.getBody()).isNotNull();
        assertThat(createUser.getBody().username()).isEqualTo(username);
        assertThat(createUser.getBody().email()).isEqualTo(email);


        ResponseEntity<LoginResponse> login = restTemplate.postForEntity("http://localhost:" + port + "/api/users/login", loginRequest, LoginResponse.class);

        assertThat(login.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(login.getBody()).isNotNull();
        assertThat(login.getBody().token()).isNotNull();
        assertThat(login.getBody().username()).isEqualTo(username);

        String token = login.getBody().token();

        HttpHeaders getUserHeaders = new HttpHeaders();
        getUserHeaders.setBearerAuth(token);
        HttpEntity<Void> profileEntity = new HttpEntity<>(getUserHeaders);

        ResponseEntity<UserAdminResponse> getUser = restTemplate.exchange(
                "http://localhost:" + port + "/api/users/profile",
                HttpMethod.GET,
                profileEntity,
                UserAdminResponse.class
        );

        assertThat(getUser.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getUser.getBody().username()).isEqualTo(username);
        assertThat(getUser.getBody().email()).isEqualTo(email);


        HttpHeaders changePasswordHeaders = new HttpHeaders();
        changePasswordHeaders.setContentType(MediaType.APPLICATION_JSON);
        changePasswordHeaders.setBearerAuth(token);
        HttpEntity<ChangePasswordRequest> passwordEntity = new HttpEntity<>(changePasswordRequest, changePasswordHeaders);

        ResponseEntity<String> changePassword = restTemplate.exchange(
                "http://localhost:" + port + "/api/users/changePassword",
                HttpMethod.PUT,
                passwordEntity,
                String.class
        );

        assertThat(changePassword.getStatusCode()).isEqualTo(HttpStatus.OK);


        HttpHeaders createFoodHeaders = new HttpHeaders();
        createFoodHeaders.setContentType(MediaType.APPLICATION_JSON);
        createFoodHeaders.setBearerAuth(token);
        HttpEntity<CreateFoodRequest> createFoodEntity = new HttpEntity<>(createFoodRequest, createFoodHeaders);

        ResponseEntity<CreateFoodResponse> createFood = restTemplate.exchange(
                "http://localhost:" + port + "/api/foods",
                HttpMethod.POST,
                createFoodEntity,
                CreateFoodResponse.class
        );

        assertThat(createFood.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createFood.getBody().name()).isEqualTo(foodName);
        assertThat(createFood.getBody().carb()).isEqualTo(carb);
        assertThat(createFood.getBody().protein()).isEqualTo(protein);
        assertThat(createFood.getBody().fat()).isEqualTo(fat);
        assertThat(createFood.getBody().calorie()).isEqualTo(calorie);
        assertThat(createFood.getBody().createdBy()).isEqualTo(createdBy);


        HttpHeaders getAllFoodsHeaders = new HttpHeaders();
        getAllFoodsHeaders.setBearerAuth(token);
        HttpEntity<Void> getAllFoodsEntity = new HttpEntity<>(getAllFoodsHeaders);

        ResponseEntity<List<FoodResponse>> getAllFoods = restTemplate.exchange(
                "http://localhost:" + port + "/api/foods",
                HttpMethod.GET,
                getAllFoodsEntity,
                new ParameterizedTypeReference<List<FoodResponse>>() {}
        );

        assertThat(getAllFoods.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getAllFoods.getBody().getFirst().name()).isEqualTo(foodName);
        assertThat(getAllFoods.getBody().getFirst().carb()).isEqualTo(carb);
        assertThat(getAllFoods.getBody().getFirst().protein()).isEqualTo(protein);
        assertThat(getAllFoods.getBody().getFirst().fat()).isEqualTo(fat);
        assertThat(getAllFoods.getBody().getFirst().calorie()).isEqualTo(calorie);
        assertThat(getAllFoods.getBody().getFirst().createdBy()).isEqualTo(createdBy);

        Long foodId = getAllFoods.getBody().getFirst().id();

        HttpHeaders addFavoriteHeaders = new HttpHeaders();
        addFavoriteHeaders.setBearerAuth(token);
        HttpEntity<Void> addFavoriteEntity = new HttpEntity<>(addFavoriteHeaders);

        ResponseEntity<FavoriteResponse> addFavorite = restTemplate.exchange(
                "http://localhost:" + port + "/api/favorites/favorites/" + foodId,
                HttpMethod.POST,
                addFavoriteEntity,
                FavoriteResponse.class
        );

        assertThat(addFavorite.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(addFavorite.getBody().username()).isEqualTo(username);
        assertThat(addFavorite.getBody().foodName()).isEqualTo(foodName);
        assertThat(addFavorite.getBody().createdAt()).isNotNull();


        HttpHeaders getFavoritesHeaders = new HttpHeaders();
        getFavoritesHeaders.setBearerAuth(token);
        HttpEntity<Void> getFavoritesEntity = new HttpEntity<>(getFavoritesHeaders);

        ResponseEntity<List<FavoriteFoodResponse>> getFavorites = restTemplate.exchange(
                "http://localhost:" + port + "/api/favorites/favoriteList",
                HttpMethod.GET,
                getFavoritesEntity,
                new ParameterizedTypeReference<List<FavoriteFoodResponse>>() {}
        );

        assertThat(getFavorites.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getFavorites.getBody().getFirst().name()).isEqualTo(foodName);
        assertThat(getFavorites.getBody().getFirst().carb()).isEqualTo(carb);
        assertThat(getFavorites.getBody().getFirst().protein()).isEqualTo(protein);
        assertThat(getFavorites.getBody().getFirst().fat()).isEqualTo(fat);
        assertThat(getFavorites.getBody().getFirst().calorie()).isEqualTo(calorie);
        assertThat(getFavorites.getBody()).hasSize(1);


        HttpHeaders removeFavoriteHeaders = new HttpHeaders();
        removeFavoriteHeaders.setBearerAuth(token);
        HttpEntity<Void> removeFavoriteEntity = new HttpEntity<>(removeFavoriteHeaders);

        ResponseEntity<FavoriteResponse> removeFavorite = restTemplate.exchange(
                "http://localhost:" + port + "/api/favorites/favorites/" + foodId,
                HttpMethod.DELETE,
                removeFavoriteEntity,
                FavoriteResponse.class
        );

        assertThat(removeFavorite.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(removeFavorite.getBody().username()).isEqualTo(username);
        assertThat(removeFavorite.getBody().foodName()).isEqualTo(foodName);
        assertThat(removeFavorite.getBody().createdAt()).isNotNull();



        getFavorites = restTemplate.exchange(
                "http://localhost:" + port + "/api/favorites/favoriteList",
                HttpMethod.GET,
                getFavoritesEntity,
                new ParameterizedTypeReference<List<FavoriteFoodResponse>>() {}
        );

        assertThat(getFavorites.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getFavorites.getBody()).hasSize(0);

    }

    @Test
    void shouldThrowException_WhenNotAuthenticated() {
        HttpHeaders getAllFoodsHeaders = new HttpHeaders();
        HttpEntity<Void> getAllFoodsEntity = new HttpEntity<>(getAllFoodsHeaders);

        ResponseEntity<List<FoodResponse>> getAllFoods = restTemplate.exchange(
                "http://localhost:" + port + "/api/foods",
                HttpMethod.GET,
                getAllFoodsEntity,
                new ParameterizedTypeReference<List<FoodResponse>>() {}

        );

        assertThat(getAllFoods.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(getAllFoods.getBody()).isNull();
    }

    @Test
    void shouldThrowException_WhenRoleIsUser() {
        String username = "username";
        String email = "user@example.com";
        String password = "raw_password";

        RegisterRequest registerRequest = new RegisterRequest(
                username,
                email,
                password
        );

        LoginRequest loginRequest = new LoginRequest(
                username,
                password
        );


        ResponseEntity<RegisterResponse> createUser = restTemplate.postForEntity("http://localhost:" + port + "/api/users/register", registerRequest, RegisterResponse.class);

        assertThat(createUser.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createUser.getBody()).isNotNull();
        assertThat(createUser.getBody().username()).isEqualTo(username);
        assertThat(createUser.getBody().email()).isEqualTo(email);


        ResponseEntity<LoginResponse> login = restTemplate.postForEntity("http://localhost:" + port + "/api/users/login", loginRequest, LoginResponse.class);

        assertThat(login.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(login.getBody()).isNotNull();
        assertThat(login.getBody().token()).isNotNull();
        assertThat(login.getBody().username()).isEqualTo(username);

        String token = login.getBody().token();

        HttpHeaders getAllUsersHeaders = new HttpHeaders();
        getAllUsersHeaders.setBearerAuth(token);
        HttpEntity<Void> getAllUsersEntity = new HttpEntity<>(getAllUsersHeaders);


        ResponseEntity<ErrorResponse> getAllUsers = restTemplate.exchange(
                "http://localhost:" + port + "/api/users",
                HttpMethod.GET,
                getAllUsersEntity,
                ErrorResponse.class

        );

        assertThat(getAllUsers.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(getAllUsers.getBody().status()).isEqualTo(403);
        assertThat(getAllUsers.getBody().error()).isEqualTo("Forbidden");

    }


}
