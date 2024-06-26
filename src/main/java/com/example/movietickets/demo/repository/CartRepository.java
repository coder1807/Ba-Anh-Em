package com.example.movietickets.demo.repository;

import com.example.movietickets.demo.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository  extends JpaRepository<CartItem, Long> {
}
