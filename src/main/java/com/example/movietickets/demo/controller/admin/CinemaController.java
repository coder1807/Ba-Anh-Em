package com.example.movietickets.demo.controller.admin;


import com.example.movietickets.demo.model.Cinema;
import com.example.movietickets.demo.service.CinemaService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@AllArgsConstructor
public class CinemaController {
    @Autowired
    private  final CinemaService cinemaService;

    //gửi response ra view add
    @GetMapping("/admin/cinemas/add")
    public String showAddForm(Model model) {
        model.addAttribute("cinema", new Cinema());
        return "/admin/cinema/cinema-add";
    }
    //gọi phương thức mapp tới form add
    @PostMapping("/admin/cinemas/add")
    public String addCinema(@Valid Cinema cinema, BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/cinema/cinema-add";
        }
        cinemaService.addCinema(cinema);
        return "redirect:/admin/cinemas";
    }
    // Hiển thị danh sách danh mục
    @GetMapping("/admin/cinemas")
    public String listCinemas(Model model) {
        List<Cinema> cinemas = cinemaService.getAllCinemas();
        model.addAttribute("cinemas", cinemas);
        model.addAttribute("title", "Danh sách rạp chiếu phim");
        return "/admin/cinema/cinema-list";
    }
    // GET request to show category edit form
    @GetMapping("/admin/cinemas/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Cinema cinema = cinemaService.getCinemaById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid cinema Id:" + id));
        model.addAttribute("cinema", cinema);
        return "/admin/cinema/cinema-edit";
    }

    // POST request to update category
    @PostMapping("/admin/cinemas/edit/{id}")
    public String updateCinema(@PathVariable("id") Long id, @Valid Cinema cinema, BindingResult result, Model model) {
        if (result.hasErrors()) {
            cinema.setId(id);
            return "/admin/cinema/cinema-edit";
        }

        cinemaService.updateCinema(cinema);
        model.addAttribute("cinemas", cinemaService.getAllCinemas());
        return "redirect:/admin/cinemas";
    }

    // GET request for deleting category
    @GetMapping("/admin/cinemas/delete/{id}")
    public String deleteCinemaById(@PathVariable("id") Long id, Model model) {
        Cinema cinema = cinemaService.getCinemaById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rap chieu Id:" + id));

        cinemaService.deleteCinemaById(id);
        model.addAttribute("cinemas", cinemaService.getAllCinemas());
        return "redirect:/admin/cinemas";
    }

}
