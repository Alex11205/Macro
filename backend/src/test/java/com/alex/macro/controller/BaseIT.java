package com.alex.macro.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest()
//@AutoConfigureRestTestClient
//@AutoConfigureTestRestTemplate
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Testcontainers
@Sql(
        statements = {
                "TRUNCATE TABLE favorite RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE food RESTART IDENTITY CASCADE",
                "TRUNCATE TABLE users RESTART IDENTITY CASCADE"
        },
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
)
public class BaseIT {

    @ServiceConnection
    protected static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine");
    static {
        postgres.start();
    }

    @Autowired
    protected MockMvc mockMvc;

//    @Autowired
//    protected ObjectMapper objectMapper;
      final ObjectMapper objectMapper = new ObjectMapper();

}
