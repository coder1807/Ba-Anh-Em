package com.example.movietickets.demo.controller.admin;

import com.example.movietickets.demo.model.Room;
import com.example.movietickets.demo.model.Seat;
import com.example.movietickets.demo.model.SeatType;
import com.example.movietickets.demo.service.SeatTypeService;
import com.example.movietickets.demo.repository.SeatTypeRepository;
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
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminSeatTypeController {
    @Autowired
    private final SeatTypeService seatTypeService;
    private final SeatTypeRepository seatTypeRepository;

    // Hiển thị danh sách danh mục
    @GetMapping("/seattypes")
    public String listCategories(Model model) {
        List<SeatType> seatTypes = seatTypeService.getAllSeatTypes();
        model.addAttribute("seattypes", seatTypes);
        model.addAttribute("title", "Danh sách loại ghế");
        return "/admin/seatType/seatType-list";
    }

    @GetMapping("/seattypes/edit/{id}")
    public String showSeattypes(@PathVariable("id") Long id, Model model) {
        SeatType seatType = seatTypeService.getSeatTypeById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid seattype Id:" + id));
        model.addAttribute("title", "Chỉnh sửa loại ghế #" + seatType.getType());
        model.addAttribute("seatType", seatType);
        return "/admin/seatType/seatType-edit";
    }

    @PostMapping("/seattypes/edit/{id}")
    public String updateTypes(@PathVariable("id") Long id, @Valid SeatType seatType, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("seatTypes", seatTypeService.getAllSeatTypes());
            return "/admin/seatType/seatType-edit";
        }

        seatTypeService.updateSeatType(seatType);
        model.addAttribute("seatTypes", seatTypeService.getAllSeatTypes());
        return "redirect:/admin/seattypes";
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
