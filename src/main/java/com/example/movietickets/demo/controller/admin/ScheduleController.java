package com.example.movietickets.demo.controller.admin;



import com.example.movietickets.demo.model.Schedule;
import com.example.movietickets.demo.service.FilmService;
import com.example.movietickets.demo.service.RoomService;
import com.example.movietickets.demo.service.ScheduleServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;
import java.util.List;

@Controller("adminScheduleController")
@AllArgsConstructor
public class ScheduleController {
    @Autowired
    private final ScheduleServiceImpl scheduleService;
    @Autowired
    private final FilmService filmService;
    @Autowired
    private final RoomService roomService;

    @GetMapping("/admin/schedules")
    public String listSchedules(Model model) {
        List<Schedule> schedules = scheduleService.getAllSchedules();
        model.addAttribute("schedules", schedules);
        model.addAttribute("title", "Danh sách lịch chiếu");
        return "/admin/schedule/schedule-list";
    }

    @GetMapping("/admin/schedules/add")
    public String showAddForm(Model model) {
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("films", filmService.getAllFilms());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "/admin/schedule/schedule-add";
    }

    @PostMapping("/admin/schedules/add")
    public String addSchedule(@Valid Schedule schedule, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("films", filmService.getAllFilms());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "/admin/schedule/schedule-add";
        }
        scheduleService.addSchedule(schedule);
        return "redirect:/admin/schedules";
    }

    @GetMapping("/admin/schedules/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Schedule schedule = scheduleService.getScheduleById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid film Id: " + id));
        model.addAttribute("schedule", schedule);
        model.addAttribute("films", filmService.getAllFilms());
        model.addAttribute("rooms", roomService.getAllRooms());
        return "/admin/schedule/schedule-edit";
    }

    @PostMapping("/admin/schedules/edit/{id}")
    public String updateSchedule(@PathVariable("id") Long id, @Valid Schedule schedule, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("films", filmService.getAllFilms());
            model.addAttribute("rooms", roomService.getAllRooms());
            return "/admin/schedule/schedule-edit";
        }
        scheduleService.updateSchedule(schedule);
        return "redirect:/admin/schedules";
    }

    @GetMapping("/admin/schedules/delete/{id}")
    public String deleteSchedule(@PathVariable("id") Long id) {
        scheduleService.deleteScheduleById(id);
        return "redirect:/admin/schedules";
    }

}
