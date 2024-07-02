package com.example.movietickets.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Booking_Detail")
public class BookingDetail {
    @Column(name = "BOOKING_DETAIL_ID")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "BOOKING_ID")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "SEAT_ID")
    private Seat seat;

    @Column(name = "PRICE")
    private Long price;

    @ManyToOne
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;
}
