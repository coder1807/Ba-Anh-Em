package com.example.movietickets.demo.repository;


import com.example.movietickets.demo.model.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface FilmRepository  extends  PagingAndSortingRepository<Film,  Long> ,JpaRepository<Film, Long>{
    default Page<Film> findAllFilmsForUser(Integer pageNo, Integer pageSize, String sortBy) {
        return findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortBy)));
    }



    @Query("SELECT f FROM Film f WHERE f.name LIKE %:keyword%")
    List<Film> searchFilmByName(@Param("keyword") String keyword);

    List<Film> findByCountry_Id(Long countryId);

    List<Film> findByCategoryId(Long categoryId);
}
