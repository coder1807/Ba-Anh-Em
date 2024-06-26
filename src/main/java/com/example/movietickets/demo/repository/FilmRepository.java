package com.example.movietickets.demo.repository;

import com.example.movietickets.demo.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FilmRepository  extends JpaRepository<Film, Long> {
}
