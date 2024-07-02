package com.example.movietickets.demo.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "SeatReservation")
public class SeatReservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RESERVATION_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SEAT_ID")
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;

    @Column(name = "RESERVED")
    private boolean reserved;

    // Constructor without parameters
    public SeatReservation() {
    }

    // Constructor with schedule ID
    public SeatReservation(Long id) {
        this.id = id;
    }

}