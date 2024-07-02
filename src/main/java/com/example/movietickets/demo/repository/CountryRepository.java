package com.example.movietickets.demo.repository;

import com.example.movietickets.demo.model.ComboFood;
import com.example.movietickets.demo.model.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    @Query("SELECT cf FROM Country cf ORDER BY cf.id DESC")
    List<Country> findAllByOrderByIdDesc();
}
