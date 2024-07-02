package com.example.movietickets.demo.controller.admin;

import com.example.movietickets.demo.model.Category;
import com.example.movietickets.demo.model.SeatType;
import com.example.movietickets.demo.service.CategoryService;
import com.example.movietickets.demo.service.SeatTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@Controller
@RequestMapping("/admin/seattypes")
@RequiredArgsConstructor
public class SeatTypeController {
    @Autowired
    private final SeatTypeService seatTypeService;

    // Hiển thị danh sách danh mục
    @GetMapping
    public String listCategories(Model model) {
        List<SeatType> seatTypes = seatTypeService.getAllSeatTypes();
        model.addAttribute("seattypes", seatTypes);
        model.addAttribute("title", "Danh sách loại ghe");
        return "/admin/seatType/seatType-list";
    }

    // GET request for deleting category
    @GetMapping("/delete/{id}")
    public String deleteSeatTypeById(@PathVariable("id") Long id, Model model) {
        SeatType seatType = seatTypeService.getSeatTypeById(id)
                .orElseThrow(() -> new IllegalArgumentException("loai ghe Id:" + id));

        seatTypeService.deleteSeatTypeById(id);
        model.addAttribute("seattypes", seatTypeService.getAllSeatTypes());
        return "redirect:/admin/seattypes/";
    }
}
