package com.example.movietickets.demo.repository;

import com.example.movietickets.demo.model.SeatType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatTypeRepository extends JpaRepository<SeatType, Long> {
}
