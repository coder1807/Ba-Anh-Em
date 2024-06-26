package com.example.movietickets.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/blog")
public class BlogController {
    @GetMapping
    public String blog() {
        return "/blog/blog";
    }

    @GetMapping("/details")
    public String Details() {
        return "/blog/blog-details";
    }
}