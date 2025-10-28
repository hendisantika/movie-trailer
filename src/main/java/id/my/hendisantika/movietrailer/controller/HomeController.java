package id.my.hendisantika.movietrailer.controller;

import id.my.hendisantika.movietrailer.entity.Movie;
import id.my.hendisantika.movietrailer.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
 * Time: 06.18
 * To change this template use File | Settings | File Templates.
 */
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {
    private final MovieRepository movieRepository;

    @GetMapping
    public ModelAndView seeHomepage() {
        List<Movie> latestMovies =
                movieRepository.findAll(PageRequest.of(0, 4, Sort.by("premiereDate").descending())).toList();
        return new ModelAndView("index")
                .addObject("latestMovies", latestMovies);
    }

    @GetMapping("movies")
    public ModelAndView listMovies(@PageableDefault(sort = "premiereDate", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<Movie> movies = movieRepository.findAll(pageable);
        return new ModelAndView("movies")
                .addObject("movies", movies);
    }

    @GetMapping("movies/{id}")
    public ModelAndView showMovieDetails(@PathVariable Integer id) {
        Movie movie = movieRepository.findById(id).get();
        return new ModelAndView("movie").addObject("movie", movie);
    }
}
