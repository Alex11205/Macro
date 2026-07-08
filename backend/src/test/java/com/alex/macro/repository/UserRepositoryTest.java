package com.alex.macro.repository;

import com.alex.macro.dto.UserAdminResponse;
import com.alex.macro.model.Role;
import com.alex.macro.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UserRepositoryTest extends BaseRepositoryTest{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testentityManager;

    @Test
    void whenDuplicateUsername_ShouldThrowException() {

        String username = "username";
        User user1 = new User(
                username,
                "user1@example.com",
                "hashed_password"
        );

        User user2 = new User(
                username,
                "user2@example.com",
                "hashed_password"
        );

        testentityManager.persistAndFlush(user1);

        assertThrows(DataIntegrityViolationException.class, () -> {

            userRepository.saveAndFlush(user2);
        });
    }

    @Test
    void whenUsernameIsNull_ShouldThrowException() {

        User user = new User(
                null,
                "user@example.com",
                "hashed_password"
        );

        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            testentityManager.persistAndFlush(user);
        });
    }

    @Test
    void whenEmailIsNotValid_ShouldThrowException() {

        String username = "username";
        User user = new User(
                username,
                "email",
                "hashed_password"
        );

        assertThrows(jakarta.validation.ConstraintViolationException.class, () -> {
            testentityManager.persistAndFlush(user);

        });
    }

    @Test
    void whenRoleIsNull_ShouldThrowException() {

        String username = "username";
        User user = new User(
                username,
                "user@example.com",
                "hashed_password"
        );

        user.setRole(null);

        assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.saveAndFlush(user);

        });
    }



    @Test
    void existsByUsername_ShouldReturnTrue_WhenUsernameExists() {

        String username = "username";
        User user = new User(
                username,
                "user@example.com",
                "hashed_password"
        );
        testentityManager.persistAndFlush(user);

        assertTrue(userRepository.existsByUsername(username));

    }

    @Test
    void existsByUsername_ShouldReturnFalse_WhenUsernameNotExist() {

        String username = "username";

        assertFalse(userRepository.existsByUsername(username));

    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUsernameExists() {
        String username = "username";
        User user = new User(
                username,
                "user@example.com",
                "hashed_password"
        );
        User savedUser = testentityManager.persistAndFlush(user);

        assertEquals(Optional.of(savedUser), userRepository.findByUsername(username));
    }

    @Test
    void findByName_ShouldReturnEmpty_WhenUsernameNotExist() {
        String username = "username";

        assertEquals(Optional.empty(), userRepository.findByUsername(username));
    }

    @Test
    void findAllUsers_ShouldReturnList_WhenListIsNotEmpty() {

        String username1 = "username1";
        String username2 = "username2";

        User user1 = new User(
                username1,
                "user1@example.com",
                "hashed_password1"
        );

        User user2 = new User(

                username2,
                "user2@example.com",
                "hashed_password2"
        );

        testentityManager.persistAndFlush(user1);

        testentityManager.persistAndFlush(user2);


        UserAdminResponse userAdminResponse1 = new UserAdminResponse(
                user1.getId(),
                username1,
                "user1@example.com",
                Role.USER
        );

        UserAdminResponse userAdminResponse2 = new UserAdminResponse(
                user2.getId(),
                username2,
                "user2@example.com",
                Role.USER
        );

        List<UserAdminResponse> userAdminResponses = new ArrayList<>(List.of(userAdminResponse1, userAdminResponse2));

        assertEquals(userAdminResponses, userRepository.findAllUsers());

    }

    @Test
    void findAllUsers_ShouldReturnEmpty_WhenListIsEmpty() {
        List<UserAdminResponse> userAdminResponses = new ArrayList<>();
        assertEquals(userAdminResponses, userRepository.findAllUsers());
    }


}
