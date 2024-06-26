package com.example.movietickets.demo.repository;

import com.example.movietickets.demo.model.ComboFood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComboFoodRepository extends JpaRepository<ComboFood, Long>
{
}
