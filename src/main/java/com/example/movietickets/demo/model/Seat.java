package com.example.movietickets.demo.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
@Table(name = "Seat")
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEAT_ID")
    private Long id;

    @Column(name = "SEAT_NUMBER")
    private String symbol;

    @Column(name = "IMAGE")
    private String image; // Thêm trường hình ảnh

    @Column(name = "STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "SEAT_TYPE_ID")
    private SeatType seattype;

    @OneToMany(mappedBy = "seat", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDetail> bookingDetails;

    // Constructor without parameters
    public Seat() {}

    // Constructor with schedule ID
    public Seat(Long id) {
        this.id = id;
    }


}