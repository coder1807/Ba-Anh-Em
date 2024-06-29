package com.example.movietickets.demo.controller;

import com.example.movietickets.demo.model.Category;
import com.example.movietickets.demo.model.Country;
import com.example.movietickets.demo.service.CategoryService;
import com.example.movietickets.demo.service.CountryService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class HomeController {

    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final CountryService countryService;

    @GetMapping("/")
    public String home(Model model) {

        List<Country> countries = countryService.getAllCountries();
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("countries", countries);

        return "/home/index";
    }
}
