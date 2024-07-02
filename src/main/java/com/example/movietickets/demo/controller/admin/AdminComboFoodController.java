package com.example.movietickets.demo.controller.admin;

import com.example.movietickets.demo.model.ComboFood;
import com.example.movietickets.demo.service.ComboFoodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminComboFoodController {

    @Autowired
    private final ComboFoodService comboFoodService;

    @GetMapping("/comboFoods")
    public String listComboFood(Model model) {
        List<ComboFood> comboFood = comboFoodService.getAllComboFood();
        model.addAttribute("comboFoods", comboFood);
        model.addAttribute("title", "Danh sách thức ăn nhanh ");
        return "/admin/combofood/food-list";
    }

    //add comboFood
    @GetMapping("/comboFoods/add")
    public String showAddComboFood(Model model) {
        model.addAttribute("title", "Thêm mới thức ăn nhanh");
        model.addAttribute("comboFood", new ComboFood());
        return "/admin/combofood/food-add";
    }

    @PostMapping("/comboFoods/add")
    public String addComboFood(@Valid @ModelAttribute ComboFood comboFood, BindingResult result, @RequestParam("poster") MultipartFile poster) throws IOException {

        if (!poster.isEmpty()) {
            try {
                String imageName = saveImageStatic(poster);
                comboFood.setPoster("/assets/img/comboFood/" + imageName); //lưu đường dẫn vào database
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        comboFoodService.addComboFood(comboFood);
        return "redirect:/admin/comboFoods";
    }

    private String saveImageStatic(MultipartFile image) throws IOException {
        Path dirImages = Paths.get("target/classes/static/assets/img/comboFood");
        if (!Files.exists(dirImages)) {
            Files.createDirectories(dirImages);
        }

        String newFileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(image.getOriginalFilename());

        Path pathFileUpload = dirImages.resolve(newFileName);
        Files.copy(image.getInputStream(), pathFileUpload,
                StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }


    // Hiển thị form sửa combofood
    @GetMapping("/comboFoods/edit/{id}")
    public String showEditComboFoodForm(@PathVariable("id") Long id, Model model) {
        ComboFood comboFood = comboFoodService.getComboFoodById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid ComboFood Id: " + id));
        model.addAttribute("title", "Chỉnh sửa thức ăn nhanh #" + comboFood.getId());
        model.addAttribute("comboFood", comboFood);
        return "/admin/combofood/food-edit";
    }

    // Cập nhật thông tin combofood
    @PostMapping("/comboFoods/edit/{id}")
    public String updateComboFood(@PathVariable("id") Long id, @Valid @ModelAttribute ComboFood comboFood, BindingResult result, @RequestParam("poster") MultipartFile poster, Model model) throws IOException {

        ComboFood existingComboFood = comboFoodService.getComboFoodById(id).orElseThrow(() -> new IllegalArgumentException("Invalid film Id:" + id));

        if (!poster.isEmpty()) {
            String imageName = saveImageStatic(poster);
            existingComboFood.setPoster("/assets/img/comboFood/" + imageName);
        }

        existingComboFood.setComboName(comboFood.getComboName());
        existingComboFood.setPrice(comboFood.getPrice());
        existingComboFood.setDescription(comboFood.getDescription());

        comboFoodService.updateComboFood(existingComboFood);
        return "redirect:/admin/comboFoods";
    }


    // Xóa comboFood
    @GetMapping("/comboFoods/delete/{id}")
    public String deleteFilm(@PathVariable("id") Long id) {
        comboFoodService.deleteComboFood(id);
        return "redirect:/admin/comboFoods";
    }
}