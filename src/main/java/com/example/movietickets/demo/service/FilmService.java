package com.example.movietickets.demo.service;


import com.example.movietickets.demo.model.Film;
import com.example.movietickets.demo.repository.FilmRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.FileSystemNotFoundException;
import java.util.List;
import java.util.Optional;


@AllArgsConstructor
@Service
public class FilmService {
    private  final FilmRepository filmRepository;

    // Retrieve all film from the database
    public List<Film> getAllFilms() {
        return filmRepository.findAll();
    }

    // Lấy film theo id

    public Optional<Film> getFilmById(Long id) {
        return filmRepository.findById(id);
    }

    // Thêm film
    public Film addFilm(Film film) throws IOException {

        return filmRepository.save(film);
    }


public Film updateFilm(Film film) {
    if (film.getId() == null) {
        throw new IllegalArgumentException("Film ID cannot be null for update");
    }
    return filmRepository.save(film);
}

    // Sửa phim
//    public Film updateFilm(Film film) {
//        if (film.getId() == null) {
//            throw new IllegalArgumentException("Film ID cannot be null for update");
//        }
//        Optional<Film> existingFilmOpt = filmRepository.findById(film.getId());
//        if (existingFilmOpt.isPresent()) {
//            Film existingFilm = existingFilmOpt.get();
//
//            // Cập nhật các trường, giữ lại những trường không bị thay đổi
//            existingFilm.setName(film.getName() != null ? film.getName() : existingFilm.getName());
//            existingFilm.setTrailer(film.getTrailer() != null ? film.getTrailer() : existingFilm.getTrailer());
//            existingFilm.setDescription(film.getDescription() != null ? film.getDescription() : existingFilm.getDescription());
//            existingFilm.setPoster(film.getPoster() != null ? film.getPoster() : existingFilm.getPoster());
//            existingFilm.setDirector(film.getDirector() != null ? film.getDirector() : existingFilm.getDirector());
//            existingFilm.setActor(film.getActor() != null ? film.getActor() : existingFilm.getActor());
//            existingFilm.setOpeningday(film.getOpeningday() != null ? film.getOpeningday() : existingFilm.getOpeningday());
//            existingFilm.setSubtitle(film.getSubtitle() != null ? film.getSubtitle() : existingFilm.getSubtitle());
//            existingFilm.setDuration(film.getDuration() != 0 ? film.getDuration() : existingFilm.getDuration());
//            existingFilm.setLimit_age(film.getLimit_age() != null ? film.getLimit_age() : existingFilm.getLimit_age());
//            existingFilm.setCountry(film.getCountry() != null ? film.getCountry() : existingFilm.getCountry());
//            existingFilm.setCategory(film.getCategory() != null ? film.getCategory() : existingFilm.getCategory());
//
//            return filmRepository.save(existingFilm);
//        } else {
//            throw new IllegalArgumentException("Film with ID " + film.getId() + " does not exist");
//        }
//    }
    // Xóa phim
    public void deleteFilm(Long filmId) {
        filmRepository.deleteById(filmId);
    }

    //find film by ID
    public Film findFilmById(Long id) {
        Optional<Film> product = filmRepository.findById(id);
        return product.orElseThrow(() -> new FileSystemNotFoundException("Product not found with id: " + id));
    }

}
