package com.example.movietickets.demo.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Combo_Food")
public class ComboFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMBO_ID")
    private Long id;

    @Column(name = "COMBO_NAME", nullable = false)
    private String comboName;

    @Column(name = "COMBO_PRICE", nullable = false)
    private Long price;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "COMBO_POSTER")
    private String poster;
}
