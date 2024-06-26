package com.example.movietickets.demo.controller;

import com.example.movietickets.demo.model.Film;
import com.example.movietickets.demo.model.Schedule;
import com.example.movietickets.demo.service.CategoryService;
import com.example.movietickets.demo.service.CountryService;
import com.example.movietickets.demo.service.FilmService;
import com.example.movietickets.demo.service.ScheduleService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
    // Hiển thị danh sách danh mục
    @GetMapping("/films")
    public String listFilms(Model model) {
        List<Film> films = filmService.getAllFilms();
        model.addAttribute("films", films);
        model.addAttribute("title", "Danh sách film");
        return "/film/film-list";
    }

    @GetMapping("/films/film-details/{id}")
    public String getFilmDetail(@PathVariable Long id, Model model) {
        Film film = filmService.findFilmById(id);
        List<String> actors = film.getActorList();
        model.addAttribute("film", film);
        model.addAttribute("actors", actors);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("countries", countryService.getAllCountries());
        return "film/film-detail";
    }



}

