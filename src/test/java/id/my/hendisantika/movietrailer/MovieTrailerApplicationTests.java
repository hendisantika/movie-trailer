package id.my.hendisantika.movietrailer;

import id.my.hendisantika.movietrailer.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class MovieTrailerApplicationTests extends AbstractIntegrationTest {

    @Test
    void contextLoads() {
        // Verify that the application context loads successfully
        assertTrue(getPostgresContainer().isRunning(), "PostgreSQL container should be running");
    }

    @Test
    void databaseConnectionIsValid() {
        // Verify that the database container is accessible
        assertTrue(getPostgresContainer().isCreated(), "PostgreSQL container should be created");
        assertTrue(getPostgresContainer().isRunning(), "PostgreSQL container should be running");
    }

}
