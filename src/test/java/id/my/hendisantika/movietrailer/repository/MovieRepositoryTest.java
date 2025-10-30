package id.my.hendisantika.movietrailer.repository;

import id.my.hendisantika.movietrailer.config.AbstractIntegrationTest;
import id.my.hendisantika.movietrailer.entity.Genre;
import id.my.hendisantika.movietrailer.entity.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for MovieRepository using Testcontainers.
 * Tests actual database operations with a PostgreSQL container.
 */
@Transactional
class MovieRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    private Genre actionGenre;
    private Genre dramaGenre;

    @BeforeEach
    void setUp() {
        // Clean up before each test
        movieRepository.deleteAll();
        genreRepository.deleteAll();

        // Set up test genres
        actionGenre = new Genre(1, "Action");
        dramaGenre = new Genre(2, "Drama");
        genreRepository.saveAll(List.of(actionGenre, dramaGenre));
    }

    @Test
    @DisplayName("Should save and retrieve a movie")
    void shouldSaveAndRetrieveMovie() {
        // Given
        Movie movie = createTestMovie(
                "The Matrix",
                "A computer hacker learns about the true nature of reality",
                LocalDate.of(1999, 3, 31),
                "m8e-FF8MkqU",
                List.of(actionGenre)
        );

        // When
        Movie savedMovie = movieRepository.save(movie);

        // Then
        assertThat(savedMovie.getId()).isNotNull();
        assertThat(savedMovie.getTitle()).isEqualTo("The Matrix");
        assertThat(savedMovie.getSinopsis()).contains("computer hacker");
        assertThat(savedMovie.getPremiereDate()).isEqualTo(LocalDate.of(1999, 3, 31));
        assertThat(savedMovie.getYoutubeTrailerId()).isEqualTo("m8e-FF8MkqU");
        assertThat(savedMovie.getGenres()).hasSize(1);
        assertThat(savedMovie.getGenres().get(0).getTitle()).isEqualTo("Action");
    }

    @Test
    @DisplayName("Should find movie by ID")
    void shouldFindMovieById() {
        // Given
        Movie movie = createTestMovie(
                "Inception",
                "A thief who steals secrets through dream technology",
                LocalDate.of(2010, 7, 16),
                "YoHD9XEInc0",
                List.of(actionGenre, dramaGenre)
        );
        Movie savedMovie = movieRepository.save(movie);

        // When
        Optional<Movie> foundMovie = movieRepository.findById(savedMovie.getId());

        // Then
        assertThat(foundMovie).isPresent();
        assertThat(foundMovie.get().getTitle()).isEqualTo("Inception");
        assertThat(foundMovie.get().getGenres()).hasSize(2);
    }

    @Test
    @DisplayName("Should find all movies")
    void shouldFindAllMovies() {
        // Given
        Movie movie1 = createTestMovie(
                "The Dark Knight",
                "Batman faces the Joker",
                LocalDate.of(2008, 7, 18),
                "EXeTwQWrcwY",
                List.of(actionGenre)
        );
        Movie movie2 = createTestMovie(
                "The Shawshank Redemption",
                "Two imprisoned men bond over years",
                LocalDate.of(1994, 9, 23),
                "6hB3S9bIaco",
                List.of(dramaGenre)
        );
        movieRepository.saveAll(List.of(movie1, movie2));

        // When
        List<Movie> movies = movieRepository.findAll();

        // Then
        assertThat(movies).hasSize(2);
        assertThat(movies).extracting(Movie::getTitle)
                .containsExactlyInAnyOrder("The Dark Knight", "The Shawshank Redemption");
    }

    @Test
    @DisplayName("Should update movie")
    void shouldUpdateMovie() {
        // Given
        Movie movie = createTestMovie(
                "Interstellar",
                "Space exploration",
                LocalDate.of(2014, 11, 7),
                "zSWdZVtXT7E",
                List.of(actionGenre)
        );
        Movie savedMovie = movieRepository.save(movie);

        // When
        savedMovie.setTitle("Interstellar (Updated)");
        savedMovie.setSinopsis("A team of explorers travel through a wormhole in space");
        Movie updatedMovie = movieRepository.save(savedMovie);

        // Then
        assertThat(updatedMovie.getId()).isEqualTo(savedMovie.getId());
        assertThat(updatedMovie.getTitle()).isEqualTo("Interstellar (Updated)");
        assertThat(updatedMovie.getSinopsis()).contains("wormhole");
    }

    @Test
    @DisplayName("Should delete movie")
    void shouldDeleteMovie() {
        // Given
        Movie movie = createTestMovie(
                "The Prestige",
                "Two magicians compete",
                LocalDate.of(2006, 10, 20),
                "o4gHCmTQDVI",
                List.of(dramaGenre)
        );
        Movie savedMovie = movieRepository.save(movie);
        Integer movieId = savedMovie.getId();

        // When
        movieRepository.deleteById(movieId);

        // Then
        Optional<Movie> deletedMovie = movieRepository.findById(movieId);
        assertThat(deletedMovie).isEmpty();
    }

    @Test
    @DisplayName("Should count movies")
    void shouldCountMovies() {
        // Given
        Movie movie1 = createTestMovie("Movie 1", "Synopsis 1", LocalDate.now(), "id1", List.of(actionGenre));
        Movie movie2 = createTestMovie("Movie 2", "Synopsis 2", LocalDate.now(), "id2", List.of(dramaGenre));
        Movie movie3 = createTestMovie("Movie 3", "Synopsis 3", LocalDate.now(), "id3", List.of(actionGenre));
        movieRepository.saveAll(List.of(movie1, movie2, movie3));

        // When
        long count = movieRepository.count();

        // Then
        assertThat(count).isEqualTo(3);
    }

    private Movie createTestMovie(String title, String sinopsis, LocalDate premiereDate,
                                  String youtubeTrailerId, List<Genre> genres) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setSinopsis(sinopsis);
        movie.setPremiereDate(premiereDate);
        movie.setYoutubeTrailerId(youtubeTrailerId);
        // Create a mutable ArrayList to avoid UnsupportedOperationException
        movie.setGenres(new java.util.ArrayList<>(genres));
        movie.setRouteCover("/covers/default.jpg");
        return movie;
    }
}
