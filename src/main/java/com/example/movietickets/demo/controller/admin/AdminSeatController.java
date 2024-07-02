package com.example.movietickets.demo.controller.admin;

import com.example.movietickets.demo.model.*;
import com.example.movietickets.demo.service.ScheduleServiceImpl;
import org.springframework.ui.Model;
import jakarta.validation.Valid;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.example.movietickets.demo.service.RoomService;
import com.example.movietickets.demo.service.SeatService;
import com.example.movietickets.demo.service.SeatTypeService;
import lombok.AllArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
//Quản li ghe cho cac phong o cac RAP

@AllArgsConstructor
@Controller("adminSeatController")
@RequestMapping("/admin")
public class AdminSeatController {
    @Autowired
    private SeatService seatService;

    @Autowired
    private ScheduleServiceImpl scheduleService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private SeatTypeService seatTypeService;

    //  private static final Logger logger = LoggerFactory.getLogger(AdminSeatController.class);

    //    @GetMapping
//    public String getAllSeats(
//            Model model) {
//       // List<Room> rooms = roomService.getAllRooms();
//        List<Seat> seats = seatService.getAllSeats();
//        model.addAttribute("seats", seats);
//        model.addAttribute("title", "Danh sách ghế");
//       // model.addAttribute("rooms", rooms); // load rooms
//        return "/admin/seat/seat-list";
//    }

    @GetMapping("/seats")
    public String getAllSeats(@RequestParam(value = "roomId", required = false) Long roomId, Model model) {
        List<Seat> seats;
        if (roomId != null) {
            seats = seatService.getSeatsByRoomId(roomId);
        } else {
            seats = seatService.getAllSeats();
        }
        model.addAttribute("title", "Danh sách ghế trong phòng");
        model.addAttribute("seats", seats);
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("selectedRoomId", roomId);
        return "/admin/seat/seat-list";
    }


    // Create new seat form
    @GetMapping("/seats/add")
    public String showCreateSeatForm(Model model) {
        model.addAttribute("title", "Thêm mới ghế");
        model.addAttribute("seat", new Seat());
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("seattypes", seatTypeService.getAllSeatTypes());
        return "/admin/seat/seat-add"; // Thymeleaf view name
    }


    //gọi phương thức mapp tới form add
    @PostMapping("/seats/add")
    public String addSeat(@Valid @ModelAttribute Seat seat, BindingResult result, @RequestParam("image") MultipartFile image, Model model) throws IOException {
        if (!image.isEmpty()) {
            try {
                String imageName = saveImageStatic(image);
                seat.setImage("/assets/img/seat/" + imageName); //lưu đường dẫn vào database
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        seatService.saveSeat(seat);
        return "redirect:/admin/seats";
    }

    private String saveImageStatic(MultipartFile image) throws IOException {
        Path dirImages = Paths.get("target/classes/static/assets/img/seat");
        if (!Files.exists(dirImages)) {
            Files.createDirectories(dirImages);
        }

        String newFileName = UUID.randomUUID() + "." + StringUtils.getFilenameExtension(image.getOriginalFilename());

        Path pathFileUpload = dirImages.resolve(newFileName);
        Files.copy(image.getInputStream(), pathFileUpload,
                StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }

    // Edit seat form
    @GetMapping("/seats/edit/{id}")
    public String showEditSeatForm(@PathVariable("id") Long id, Model model) throws IOException {
        Seat seat = seatService.getSeatById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid seat Id: " + id));
        model.addAttribute("title", "Chỉnh sửa ghế #" + seat.getId());
        model.addAttribute("seat", seat);
        model.addAttribute("rooms", roomService.getAllRooms());
        model.addAttribute("seattypes", seatTypeService.getAllSeatTypes());
        return "/admin/seat/seat-edit"; // Thymeleaf view name
    }

    // Update seat
    @PostMapping("/seats/edit/{id}")
    public String updateSeat(@PathVariable("id") Long id, @Valid @ModelAttribute Seat seat, BindingResult result, Model model, @RequestParam("image") MultipartFile image) throws IOException {
        Seat existingSeat = seatService.getSeatById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid seat Id: " + id));
        if (!image.isEmpty()) {
            String imageName = saveImageStatic(image);
            existingSeat.setImage("/assets/img/seat/" + imageName);
        }
        existingSeat.setSeattype(seat.getSeattype());
        existingSeat.setStatus(seat.getStatus());
        existingSeat.setRoom(seat.getRoom());
        existingSeat.setSeattype(seat.getSeattype());
        seatService.updateSeat(existingSeat);
        return "redirect:/admin/seats";
    }

    // Delete seat
    @GetMapping("/seats/delete/{id}")
    public String deleteSeat(@PathVariable Long id, Model model) {
        Seat seat = seatService.getSeatById(id)
                .orElseThrow(() -> new IllegalArgumentException("Seat Id:" + id));

        seatService.deleteSeat(id);
        model.addAttribute("seats", seatService.getAllSeats());
        return "redirect:/admin/seats";
    }
}
