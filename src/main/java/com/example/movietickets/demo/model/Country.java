package com.example.movietickets.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;


@Data
@Entity
@Table(name = "Country")
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COUNTRY_ID")
    private Long id;

    @Column(name = "COUNTRY_NAME", nullable = false)
    @NotBlank(message = "Tên quốc gia là bắt buộc")
    @Size(max = 50)
    private String name;

    @OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Film> films;

    @Override
    public String toString() {
        return "Country{id=" + id + ", name='" + name + "'}";
    }

//    @Override
//    public String toString() {
//        return "Country{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                '}';
//    }
}


