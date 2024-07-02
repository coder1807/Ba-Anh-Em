package com.example.movietickets.demo.repository;

import com.example.movietickets.demo.model.Booking;
import com.example.movietickets.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("SELECT COUNT(id) AS total FROM Booking")
    Long getCountBooking();

    @Query("SELECT SUM(b.price) FROM Booking b")
    Long getTotalPrice();

    List<Booking> findByUser(User user);

    List<Booking> findAllWithComboFoodByUser(User user);
}
