package com.example.movietickets.demo.repository;

import com.example.movietickets.demo.model.Booking;
import com.example.movietickets.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>
{
    List<Booking> findByUser(User user);

}
