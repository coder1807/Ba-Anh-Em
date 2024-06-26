package com.example.movietickets.demo.repository;

import com.example.movietickets.demo.model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CinemaRepository  extends JpaRepository<Cinema, Long> {
}
