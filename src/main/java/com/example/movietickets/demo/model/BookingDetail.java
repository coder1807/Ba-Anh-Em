package com.example.movietickets.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Booking_Detail")
public class BookingDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BOOKING_ID")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "FILM_ID")
    private Film film;

    @Column(name = "TOTAL_PRICE")
    private Long price;

    @Column(name = "SOLUONGVE")
    private int soLuong;

    @Column(name = "COMBO_NAME")
    private String comboName;
}