package com.example.movietickets.demo.controller.admin;

import com.example.movietickets.demo.model.Category;
import com.example.movietickets.demo.model.Film;
import com.example.movietickets.demo.service.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller("adminFilmController")
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminFilmController {
    @Autowired
    private final FilmService filmService;
    @Autowired
    private final CountryService countryService;
    @Autowired
    private final CategoryService categoryService;
    @Autowired
    private final ScheduleServiceImpl scheduleService;

    // Hiển thị danh sách danh mục
    @GetMapping("/films")
    public String listFilms(Model model) {
        List<Film> films = filmService.getAllFilms();
        model.addAttribute("films", films);
        model.addAttribute("title", "Danh sách phim sắp chiếu");
        return "/admin/film/film-list";
    }

    // add film
    @GetMapping("/films/add")
    public String showAddFilm(Model model) {
        List<Category> selectedCategories = new ArrayList<>();
        model.addAttribute("film", new Film());
        model.addAttribute("title", "Thêm mới Phim");
        model.addAttribute("countries", countryService.getAllCountries());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("categories_selected", selectedCategories);
        return "/admin/film/film-add";
    }

    @PostMapping("/films/add")
    public String addFilm(@Valid @ModelAttribute Film film, BindingResult result,
                          @RequestParam("poster") MultipartFile poster) throws IOException {

        if (!poster.isEmpty()) {
            try {
                String imageName = saveImageStatic(poster);
                film.setPoster("/assets/img/movie/" + imageName); // lưu đường dẫn vào database
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // Chuyển đổi danh sách các ID thành danh sách các đối tượng Category
        List<Category> categories = categoryService.findAllById(film.getCategoryIds());
        film.setCategories(categories);

        filmService.addFilm(film);
        return "redirect:/admin/films";
    }

    private String saveImageStatic(MultipartFile image) throws IOException {
        Path dirImages = Paths.get("target/classes/static/assets/img/movie");
        if (!Files.exists(dirImages)) {
            Files.createDirectories(dirImages);
        }

        String newFileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(image.getOriginalFilename());

        Path pathFileUpload = dirImages.resolve(newFileName);
        Files.copy(image.getInputStream(), pathFileUpload,
                StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }

    // Hiển thị form sửa phim
    @GetMapping("/films/edit/{id}")
    public String showEditFilmForm(@PathVariable("id") Long id, Model model) {
        Film film = filmService.getFilmById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid film Id: " + id));
        // Gán danh sách các ID thể loại vào đối tượng film
        List<Long> categoryIds = film.getCategories().stream()
                .map(Category::getId)
                .collect(Collectors.toList());
        if (categoryIds == null) {
            categoryIds = new ArrayList<>();
        }
        film.setCategoryIds(categoryIds);
        model.addAttribute("film", film);
        model.addAttribute("title", "Chỉnh sửa Phim #" + film.getId());
        model.addAttribute("countries", countryService.getAllCountries());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/admin/film/film-edit";
    }

    // Cập nhật thông tin phim
    @PostMapping("/films/edit/{id}")
    public String updateFilm(@PathVariable("id") Long id, @Valid @ModelAttribute Film film, BindingResult result,
                             @RequestParam("poster") MultipartFile poster, Model model) throws IOException {

        Film existingFilm = filmService.getFilmById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid film Id:" + id));

        if (!poster.isEmpty()) {
            String imageName = saveImageStatic(poster);
            existingFilm.setPoster("/assets/img/movie/" + imageName);
        }

        existingFilm.setName(film.getName());
        existingFilm.setTrailer(film.getTrailer());
        existingFilm.setDescription(film.getDescription());
        existingFilm.setDirector(film.getDirector());
        existingFilm.setActor(film.getActor());
        existingFilm.setOpeningday(film.getOpeningday());
        existingFilm.setSubtitle(film.getSubtitle());
        existingFilm.setDuration(film.getDuration());
        existingFilm.setLimit_age(film.getLimit_age());
        existingFilm.setQuanlity(film.getQuanlity());
        existingFilm.setCountry(film.getCountry());
        List<Category> categories = categoryService.findAllById(film.getCategoryIds());
        existingFilm.setCategories(categories);
        filmService.updateFilm(existingFilm);
        return "redirect:/admin/films";
    }

    // Xóa phim
    @Transactional // phương thức để liên hệ với bảng con => xóa lịch chiếu
    @GetMapping("/films/delete/{id}")
    public String deleteFilm(@PathVariable("id") Long id) {
        scheduleService.deleteByFilmId(id); // xóa suất chiếu của phim trước
        filmService.deleteFilm(id);
        return "redirect:/admin/films";
    }
}
