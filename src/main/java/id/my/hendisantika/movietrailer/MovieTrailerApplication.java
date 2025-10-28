package id.my.hendisantika.movietrailer;

import id.my.hendisantika.movietrailer.entity.Genre;
import id.my.hendisantika.movietrailer.repository.GenreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.thymeleaf.dialect.springdata.SpringDataDialect;

import java.util.Arrays;

@SpringBootApplication
public class MovieTrailerApplication {

    static void main(String[] args) {
        SpringApplication.run(MovieTrailerApplication.class, args);
    }

    @Bean
    public SpringDataDialect springDataDialect() {
        return new SpringDataDialect();
    }

    @Bean
    public CommandLineRunner initGenre(GenreRepository genreRepository) {
        return (args) -> {
            genreRepository.saveAll(Arrays.asList(
                    new Genre(1, "ACTION"),
                    new Genre(2, "COMEDY"),
                    new Genre(3, "HORROR"),
                    new Genre(4, "THRILLER"),
                    new Genre(5, "ADVENTURE")
            ));
        };
    }
}
