package com.example.movietickets.demo.controller;

import com.example.movietickets.demo.model.*;
import com.example.movietickets.demo.service.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@AllArgsConstructor
@Controller("userFilmController")
public class FilmController {
    @Autowired
    private  final FilmService filmService;
    @Autowired
    private  final CountryService countryService;
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final ScheduleService scheduleService;
    @Autowired
    private final RatingService ratingService;


    // Hiển thị danh sách danh mục
    @GetMapping("/films")
    public String listFilms(Model model) {
        List<Film> films = filmService.getAllFilms();
        List<Country> countries = countryService.getAllCountries();
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("films", films);
        model.addAttribute("countries", countries);
        model.addAttribute("title", "Danh sách film");
        return "/film/film-list";
    }

    @GetMapping("/films/film-details/{id}")
    public String getFilmDetail(@PathVariable Long id, Model model) {
        Film film = filmService.findFilmById(id);
        List<Rating> ratings = ratingService.getAllRatingByFilmId(id);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // kiểm tra xem người dùng đó comment chưa
        boolean hasRated = ratingService.hasUserRatedFilm(currentUsername, film.getId());
        model.addAttribute("hasRated", hasRated);

        // tính số lượng trung bình star
        Double averageRating = ratingService.getAverageRating(film.getId());
        int averageRatingInteger = (int) Math.floor(averageRating != null ? averageRating : 0);
        model.addAttribute("averageRating", averageRatingInteger);

        model.addAttribute("film", film);
        model.addAttribute("ratings", ratings);
        model.addAttribute("rating", new Rating());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("countries", countryService.getAllCountries());

        return "film/film-detail";
    }

    @GetMapping("/films/by-country")
    public String getFilmsByCountry(Model model, @RequestParam("countryId") Long countryId) {
        List<Film> films = filmService.getFilmsByCountryId(countryId);
        List<Country> countries = countryService.getAllCountries();
        List<Category> categories = categoryService.getAllCategories();


        model.addAttribute("categories", categories);
        model.addAttribute("films", films);
        model.addAttribute("countries", countries);
        model.addAttribute("countryId", countryId);
        return "film/films-by-country";
    }

    @GetMapping("/films/by-category/{id}")
    public String getFilmsByCategory(Model model, @PathVariable("id") Long categoryId) {
        List<Film> films = filmService.getFilmsByCategoryId(categoryId);
        List<Country> countries = countryService.getAllCountries();
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);


        model.addAttribute("films", films);
        model.addAttribute("countries", countries);
        return "film/films-by-category";  // Tên template view để hiển thị danh sách phim theo category
    }

}
