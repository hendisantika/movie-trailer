package id.my.hendisantika.movietrailer.controller;

import id.my.hendisantika.movietrailer.entity.Genre;
import id.my.hendisantika.movietrailer.entity.Movie;
import id.my.hendisantika.movietrailer.repository.GenreRepository;
import id.my.hendisantika.movietrailer.repository.MovieRepository;
import id.my.hendisantika.movietrailer.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * Project : movie-trailer
 * User: hendisantika
 * Link: s.id/hendisantika
 * Email: hendisantika@yahoo.co.id
 * Telegram : @hendisantika34
 * Date: 29/10/25
 * Time: 06.16
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final MovieRepository movieRepository;

    private final GenreRepository genreRepository;

    private final WarehouseService warehouseService;

    @GetMapping
    public ModelAndView seeHomepage(@PageableDefault(sort = "title", size = 5) Pageable pageable) {
        Page<Movie> movies = movieRepository.findAll(pageable);
        return new ModelAndView("admin/index").addObject("movies", movies);
    }

    @GetMapping("movies/new")
    public ModelAndView showNewFilmForm() {
        List<Genre> genres = genreRepository.findAll(Sort.by("title"));
        return new ModelAndView("admin/new-movie")
                .addObject("movie", new Movie())
                .addObject("genres", genres);
    }

    @PostMapping("/movies")
    public ModelAndView registerMovie(@Validated Movie movie, BindingResult bindingResult) {
        if (bindingResult.hasErrors() || movie.getFrontPage().isEmpty()) {
            if (movie.getFrontPage().isEmpty()) {
                bindingResult.rejectValue("frontPage", "MultipartNotEmpty");
            }

            List<Genre> genres = genreRepository.findAll(Sort.by("title"));
            return new ModelAndView("admin/new-movie")
                    .addObject("movie", movie)
                    .addObject("genres", genres);
        }

        String routeCover = warehouseService.storeFile(movie.getFrontPage());
        movie.setRouteCover(routeCover);

        movieRepository.save(movie);
        return new ModelAndView("redirect:/admin");
    }

    @GetMapping("/movies/{id}/edit")
    public ModelAndView showMovieEditForm(@PathVariable Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));
        List<Genre> genres = genreRepository.findAll(Sort.by("title"));

        return new ModelAndView("admin/edit-movie")
                .addObject("movie", movie)
                .addObject("genres", genres);
    }

    @PostMapping("/movies/{id}/edit")
    @Transactional
    public ModelAndView updateMovie(@PathVariable Integer id, @Validated Movie movie, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<Genre> genres = genreRepository.findAll(Sort.by("title"));
            return new ModelAndView("admin/edit-movie")
                    .addObject("movie", movie)
                    .addObject("genres", genres);
        }

        Movie movieDB = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));
        movieDB.setTitle(movie.getTitle());
        movieDB.setSinopsis(movie.getSinopsis());
        movieDB.setPremiereDate(movie.getPremiereDate());
        movieDB.setYoutubeTrailerId(movie.getYoutubeTrailerId());
        movieDB.setGenres(movie.getGenres());

        if (!movie.getFrontPage().isEmpty()) {
            warehouseService.deleteArchive(movieDB.getRouteCover());
            String routeCover = warehouseService.storeFile(movie.getFrontPage());
            movieDB.setRouteCover(routeCover);
        }

        movieRepository.save(movieDB);
        return new ModelAndView("redirect:/admin");
    }

    @PostMapping("/movies/{id}/delete")
    @Transactional
    public String deleteMovie(@PathVariable Integer id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found with id: " + id));

        String coverPath = movie.getRouteCover();
        movieRepository.delete(movie);

        // Delete the cover image after successful deletion from database
        if (coverPath != null && !coverPath.startsWith("http")) {
            warehouseService.deleteArchive(coverPath);
        }

        return "redirect:/admin";
    }
}
