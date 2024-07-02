package com.example.movietickets.demo.controller.admin;

import com.example.movietickets.demo.model.Booking;
import com.example.movietickets.demo.model.Schedule;
import com.example.movietickets.demo.service.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.List;

@Controller()
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminBookingController {
    @Autowired
    private final ScheduleServiceImpl scheduleService;
    @Autowired
    private final FilmService filmService;
    @Autowired
    private final RoomService roomService;
    @Autowired
    private final BookingService bookingService;
    @Autowired
    private final UserService userService;

    @GetMapping("/bookings")
    public String listSchedules(Model model) {
        List<Booking> bookings = bookingService.getAll();
        model.addAttribute("bookings", bookings);
        model.addAttribute("title", "Danh sách đặt vé");
        return "/admin/booking/booking-list";
    }

}