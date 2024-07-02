package com.example.movietickets.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long id;

    @Column(name = "CATEGORY_NAME", nullable = false)
    @NotBlank(message = "Tên thể loại là bắt buộc")
    @Size(max = 100)
    private String name;

    @ManyToMany(mappedBy = "categories")
    private List<Film> filmList;

    @Override
    public String toString() {
        return "Category{id=" + id + ", name='" + name + "'}";
    }

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Film> films;
}
