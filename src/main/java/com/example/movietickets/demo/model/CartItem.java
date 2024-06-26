package com.example.movietickets.demo.model;


import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "CartItem")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CARTITEM_ID")
    private int id;

    private int quantity;
    private  long price;

    @ManyToOne
    @JoinColumn(name = "FILM_ID")
    private Film film;

    @ManyToOne
    @JoinColumn(name = "ROOM_ID")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "SCHEDULE_ID")
    private Schedule schedule;

}
