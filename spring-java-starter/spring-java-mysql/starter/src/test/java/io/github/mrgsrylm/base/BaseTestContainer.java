package io.github.mrgsrylm.base;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class BaseTestContainer {
    static MySQLContainer<?> MYSQL_CONTAINER = new MySQLContainer<>("mysql:8.0.31");

    @BeforeAll
    static void beforeAll() {
        MYSQL_CONTAINER.withReuse(true);
        MYSQL_CONTAINER.start();
    }

    @DynamicPropertySource
    private static void overrideProps(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.username", MYSQL_CONTAINER::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", MYSQL_CONTAINER::getPassword);
        dynamicPropertyRegistry.add("spring.datasource.url", MYSQL_CONTAINER::getJdbcUrl);
    }
}
