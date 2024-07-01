package com.example.movietickets.demo.controller;

import com.example.movietickets.demo.model.Film;
import com.example.movietickets.demo.model.Schedule;
import com.example.movietickets.demo.model.Seat;
import com.example.movietickets.demo.service.RoomService;
import com.example.movietickets.demo.service.ScheduleServiceImpl;
import com.example.movietickets.demo.service.SeatService;
import com.example.movietickets.demo.service.SeatTypeService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/seats")
@AllArgsConstructor
@Controller("userSeatController")
public class SeatController {
    @Autowired
    private SeatService seatService;

    @Autowired
    private ScheduleServiceImpl scheduleService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private SeatTypeService seatTypeService;

    @GetMapping
    public String getSeatsByRoomId(@RequestParam(value = "roomId", required = false) Long roomId, Model model) {
        List<Seat> seats;
        if (roomId != null) {
            seats = seatService.getSeatsByRoomIdDistinct(roomId);
        } else {
            seats = seatService.getAllSeats();
        }
        model.addAttribute("seats", seats);
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("selectedRoomId", roomId);
        return "/seat/seat-list";
    }

    // SeatController
    @GetMapping("/schedules/{scheduleId}")
    public String getSeatsBySchedule(@PathVariable Long scheduleId, Model model) {
        Optional<Schedule> optionalSchedule = scheduleService.getScheduleById(scheduleId);
        Film film = optionalSchedule.map(Schedule::getFilm).orElse(null);//map schedule de lay thong tin film
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String currentTime = LocalTime.now().format(formatter);
        //kiem tra biến optional Schedule co chua value => get values
        if (optionalSchedule.isPresent()) {
            Schedule schedule = optionalSchedule.get();
            Long roomId = schedule.getRoom().getId();
            List<Seat> seats = seatService.getSeatsByRoomIdDistinct(roomId);
            // Add information
            String cinemaName = schedule.getRoom().getCinema().getName();
            String cinemaAddress = schedule.getRoom().getCinema().getAddress();
            String roomName = schedule.getRoom().getName();
            // Group seats theo type
            Map<String, List<Seat>> seatsByType = seats.stream().collect(Collectors.groupingBy(seat -> seat.getSeattype().getType()));

            model.addAttribute("currentTime", currentTime);
            model.addAttribute("seats", seats);
            model.addAttribute("film", film);
            model.addAttribute("schedule", schedule);
            model.addAttribute("selectedScheduleId", scheduleId);
            model.addAttribute("selectedRoomId", roomId);
            model.addAttribute("seatsByType", seatsByType);
            model.addAttribute("cinemaName", cinemaName);
            model.addAttribute("cinemaAddress", cinemaAddress);
            model.addAttribute("roomName", roomName);
            return "/seat/seat-choose"; // chuyen den trang chon ghe
        } else {
            // neu khong tim thay schedule
            return "redirect:/404"; // Redirect
        }
    }

}
