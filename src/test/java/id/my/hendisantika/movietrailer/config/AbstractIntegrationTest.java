package id.my.hendisantika.movietrailer.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Abstract base class for integration tests using Testcontainers.
 * This class sets up a PostgreSQL container that is shared across all tests.
 * Extend this class for integration tests that require database access.
 */
@SpringBootTest
@ActiveProfiles("test")
public abstract class AbstractIntegrationTest {

    private static final PostgreSQLContainer<?> postgresContainer;

    static {
        postgresContainer = new PostgreSQLContainer<>("postgres:18.0-alpine3.22")
                .withDatabaseName("movie_trailers_test")
                .withUsername("testuser")
                .withPassword("testpass")
                .withReuse(true);
        postgresContainer.start();
    }

    @DynamicPropertySource
    static void registerDynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
    }

    protected static PostgreSQLContainer<?> getPostgresContainer() {
        return postgresContainer;
    }
}
