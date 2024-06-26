package com.example.movietickets.demo.controller.admin;

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
import java.util.List;
import java.util.UUID;

@Controller("adminFilmController")
@AllArgsConstructor
public class FilmController {
    @Autowired
    private  final FilmService filmService;
    @Autowired
    private  final CountryService countryService;
    @Autowired
    private final CategoryService categoryService;

    @Autowired
    private final ScheduleServiceImpl scheduleService;
    // Hiển thị danh sách danh mục
    @GetMapping("/admin/films")
    public String listFilms(Model model) {
        List<Film> films = filmService.getAllFilms();
        model.addAttribute("films", films);
        model.addAttribute("title", "Danh sách film");
        return "/admin/film/film-list";
    }

    //add film
    @GetMapping("/admin/films/add")
    public String showAddFilm(Model model){
        model.addAttribute("film", new Film());
        model.addAttribute("countries", countryService.getAllCountries()); // Thêm danh sách quốc gia
        model.addAttribute("categories", categoryService.getAllCategories()); // Thêm danh sách thể loại
        return "/admin/film/film-add";
    }
    @PostMapping("/admin/films/add")
    public String addFilm(@Valid @ModelAttribute Film film,  BindingResult result,@RequestParam("poster") MultipartFile poster) throws IOException {

        if (!poster.isEmpty()) {
            try {
                String imageName = saveImageStatic(poster);
                film.setPoster("/assets/img/movie/" + imageName); //lưu đường dẫn vào database
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        filmService.addFilm(film);
        return "redirect:/admin/films";
    }

    private String saveImageStatic(MultipartFile image) throws IOException {
        Path dirImages = Paths.get("target/classes/static/assets/img/movie");
        if (!Files.exists(dirImages)) {
            Files.createDirectories(dirImages);
        }

        String newFileName = UUID.randomUUID()+ "." + StringUtils.getFilenameExtension(image.getOriginalFilename());

        Path pathFileUpload = dirImages.resolve(newFileName);
        Files.copy(image.getInputStream(), pathFileUpload,
                StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }

    // Hiển thị form sửa phim
    @GetMapping("/admin/films/edit/{id}")
    public String showEditFilmForm(@PathVariable("id") Long id, Model model) {
        Film film = filmService.getFilmById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid film Id: " + id));
        model.addAttribute("film", film);
        model.addAttribute("countries", countryService.getAllCountries());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "/admin/film/film-edit";
    }

    // Cập nhật thông tin phim
    @PostMapping("/admin/films/edit/{id}")
    public String updateFilm(@PathVariable("id") Long id, @Valid @ModelAttribute Film film, BindingResult result, @RequestParam("poster") MultipartFile poster, Model model) throws IOException {
//        if (result.hasErrors()) {
//            return "/admin/film/film-edit";
//        }
        Film existingFilm = filmService.getFilmById(id).orElseThrow(() -> new IllegalArgumentException("Invalid film Id:" + id));


        if (!poster.isEmpty()) {
            String imageName = saveImageStatic(poster);
            existingFilm.setPoster("/assets/img/movie/" + imageName);}
//        } else {
//            // Giữ nguyên poster hiện tại nếu không upload hình ảnh mới
//            film.setPoster(filmService.getFilmById(id).get().getPoster());
//        }

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
        existingFilm.setCategory(film.getCategory());
        filmService.updateFilm(existingFilm);
        return "redirect:/admin/films";
    }

    // Xóa phim
    @Transactional//phương thức để liên hệ với bảng con => xóa lịch chiếu
    @GetMapping("/admin/films/delete/{id}")
    public String deleteFilm(@PathVariable("id") Long id) {
        scheduleService.deleteByFilmId(id); //xóa suất chiếu của phim trước
        filmService.deleteFilm(id);
        return "redirect:/admin/films";
    }

}

