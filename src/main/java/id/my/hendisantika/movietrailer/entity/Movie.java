package id.my.hendisantika.movietrailer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : movie-trailer
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 29/10/25
 * Time: 06.10
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    private String title;

    @NotBlank
    private String sinopsis;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate premiereDate;

    @NotBlank
    private String youtubeTrailerId;

    private String routeCover;

    @NotEmpty
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "genre_movie", joinColumns = @JoinColumn(name = "movie_id"), inverseJoinColumns =
    @JoinColumn(name = "id_genre"))
    private List<Genre> genres;

    @Transient
    private MultipartFile frontPage;
}
