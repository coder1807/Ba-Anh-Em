package com.example.movietickets.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "Booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKING_ID")
    private Long  id;

    @Column(name = "FULL_NAME", nullable = false)
    private String fullname;

    @Column(name = "FILM_NAME")
    private String filmName;

    @Column(name = "FILM_POSTER")
    private String filmPoster;

    @Column(name = "LICH_CHIEU")
    private LocalDateTime startTime;

    @Column(name = "CREAT_AT")
   private LocalDateTime createAt;

    @Column(name = "UPDATE_AT")
    private LocalDateTime updateAT;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "SOLUONG")
    private int soLuong;

    @Column(name = "SEAT_TYPE")
    private String seatType;

    @ManyToOne
    @JoinColumn(name = "COMBO_ID")
    private ComboFood comboFood;
}
