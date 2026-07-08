package com.alex.macro.repository;

import com.alex.macro.dto.UserAdminResponse;
import com.alex.macro.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);



    @Query("""
        SELECT new com.alex.macro.dto.UserAdminResponse(
            user.id,
            user.username,
            user.email,
            user.role
        )  
        FROM User user
""")
    List<UserAdminResponse> findAllUsers();

}
