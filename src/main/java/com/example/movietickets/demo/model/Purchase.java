package com.example.movietickets.demo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@Data
@Getter
@Setter
public class Purchase {
    private Seat seat;
    private String seatSymbols;
    private Long totalPrice;
    private String filmTitle;
    private String category;
    private String poster;
    private String cinemaName;
    private String cinemaAddress;
    private String roomName;
    private String startTime;

    public Purchase( String seatSymbols, String filmTitle, String poster, String category, Long totalPrice, String cinemaAddress, String cinemaName, String startTime, String roomName) {

        this.seatSymbols = seatSymbols;
        this.filmTitle = filmTitle;
        this.poster = poster;
        this.category = category;
        this.cinemaName = cinemaName;
        this.cinemaAddress = cinemaAddress;
        this.roomName = roomName;
        this.totalPrice = totalPrice;
        this.startTime = startTime;
    }


}
