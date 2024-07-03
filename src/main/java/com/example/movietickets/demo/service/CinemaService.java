package com.example.movietickets.demo.service;

import com.example.movietickets.demo.model.Category;
import com.example.movietickets.demo.model.Cinema;
import com.example.movietickets.demo.model.Room;
import com.example.movietickets.demo.repository.CinemaRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CinemaService {

    private final CinemaRepository cinemaRepository;

    public List<Cinema> getAllCinemas() {
        return cinemaRepository.findAllByOrderByIdDesc();
    }

    public Optional<Cinema> getCinemaById(Long id) {
        return cinemaRepository.findById(id);
    }

    public void addCinema(Cinema cinema) {
        cinemaRepository.save(cinema);
    }

//    public void updateCinema(@NotNull Cinema cinema) {
//        Cinema existingCinema = cinemaRepository.findById(cinema.getId())
//                .orElseThrow(() -> new IllegalStateException("Cinema with ID " + cinema.getId() + " does not exist."));
//        existingCinema.setName(cinema.getName());
//        existingCinema.setAddress(cinema.getAddress());
//        existingCinema.setMap(cinema.getMap());
//        // Quản lý bộ sưu tập rooms
//        cinemaRepository.save(existingCinema);
//    }
public void updateCinema(@NotNull Cinema cinema) {
    Cinema existingCinema = cinemaRepository.findById(cinema.getId())
            .orElseThrow(() -> new IllegalStateException("Cinema with ID " + cinema.getId() + " does not exist."));

    // Cập nhật thông tin của existingCinema
    existingCinema.setName(cinema.getName());
    existingCinema.setAddress(cinema.getAddress());
    existingCinema.setMap(cinema.getMap());

    // Quản lý bộ sưu tập rooms
    // Xóa tất cả các phòng hiện tại trong existingCinema
    existingCinema.getRooms().clear();

    // Thêm các phòng từ đối tượng cinema vào existingCinema
    for (Room room : cinema.getRooms()) {
        room.setCinema(existingCinema);
        existingCinema.getRooms().add(room);
    }

    cinemaRepository.save(existingCinema);
}

    public void deleteCinemaById(Long id) {
        if (!cinemaRepository.existsById(id)) {
            throw new IllegalStateException("Cinema with ID " + id + " does not exist.");
        }
        cinemaRepository.deleteById(id);
    }
}