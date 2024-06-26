package com.example.movietickets.demo.repository;

import com.example.movietickets.demo.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CountryRepository  extends JpaRepository<Country, Long> {
}
