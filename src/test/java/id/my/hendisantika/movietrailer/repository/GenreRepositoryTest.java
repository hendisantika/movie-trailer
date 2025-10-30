package id.my.hendisantika.movietrailer.repository;

import id.my.hendisantika.movietrailer.config.AbstractIntegrationTest;
import id.my.hendisantika.movietrailer.entity.Genre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for GenreRepository using Testcontainers.
 * Tests actual database operations with a PostgreSQL container.
 */
@Transactional
class GenreRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private GenreRepository genreRepository;

    @BeforeEach
    void setUp() {
        genreRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save and retrieve a genre")
    void shouldSaveAndRetrieveGenre() {
        // Given
        Genre genre = new Genre(1, "Action");

        // When
        Genre savedGenre = genreRepository.save(genre);

        // Then
        assertThat(savedGenre.getId()).isEqualTo(1);
        assertThat(savedGenre.getTitle()).isEqualTo("Action");
    }

    @Test
    @DisplayName("Should find genre by ID")
    void shouldFindGenreById() {
        // Given
        Genre genre = new Genre(2, "Comedy");
        genreRepository.save(genre);

        // When
        Optional<Genre> foundGenre = genreRepository.findById(2);

        // Then
        assertThat(foundGenre).isPresent();
        assertThat(foundGenre.get().getTitle()).isEqualTo("Comedy");
    }

    @Test
    @DisplayName("Should find all genres")
    void shouldFindAllGenres() {
        // Given
        Genre action = new Genre(1, "Action");
        Genre drama = new Genre(2, "Drama");
        Genre comedy = new Genre(3, "Comedy");
        genreRepository.saveAll(List.of(action, drama, comedy));

        // When
        List<Genre> genres = genreRepository.findAll();

        // Then
        assertThat(genres).hasSize(3);
        assertThat(genres).extracting(Genre::getTitle)
                .containsExactlyInAnyOrder("Action", "Drama", "Comedy");
    }

    @Test
    @DisplayName("Should update genre")
    void shouldUpdateGenre() {
        // Given
        Genre genre = new Genre(4, "Thriller");
        Genre savedGenre = genreRepository.save(genre);

        // When
        savedGenre.setTitle("Suspense Thriller");
        Genre updatedGenre = genreRepository.save(savedGenre);

        // Then
        assertThat(updatedGenre.getId()).isEqualTo(4);
        assertThat(updatedGenre.getTitle()).isEqualTo("Suspense Thriller");
    }

    @Test
    @DisplayName("Should delete genre")
    void shouldDeleteGenre() {
        // Given
        Genre genre = new Genre(5, "Horror");
        genreRepository.save(genre);

        // When
        genreRepository.deleteById(5);

        // Then
        Optional<Genre> deletedGenre = genreRepository.findById(5);
        assertThat(deletedGenre).isEmpty();
    }

    @Test
    @DisplayName("Should count genres")
    void shouldCountGenres() {
        // Given
        genreRepository.saveAll(List.of(
                new Genre(1, "Action"),
                new Genre(2, "Drama"),
                new Genre(3, "Comedy"),
                new Genre(4, "Thriller"),
                new Genre(5, "Romance")
        ));

        // When
        long count = genreRepository.count();

        // Then
        assertThat(count).isEqualTo(5);
    }

    @Test
    @DisplayName("Should check if genre exists")
    void shouldCheckIfGenreExists() {
        // Given
        Genre genre = new Genre(6, "Documentary");
        genreRepository.save(genre);

        // When
        boolean exists = genreRepository.existsById(6);
        boolean notExists = genreRepository.existsById(999);

        // Then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}
