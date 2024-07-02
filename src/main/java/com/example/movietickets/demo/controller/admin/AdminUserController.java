package com.example.movietickets.demo.controller.admin;

import com.example.movietickets.demo.model.User;
import com.example.movietickets.demo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {
    @Autowired
    private final UserService userService;

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = userService.getAllUsers();
        model.addAttribute("title", "Danh sách người dùng");
        model.addAttribute("users", listUsers);
        return "/admin/user/user-list";
    }

    @GetMapping("/users/detail/{id}")
    public String userDetail(@PathVariable("id") Long id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("title", "Chi tiết người dùng #" + user.getId());
        model.addAttribute("user", user);
        return "/admin/user/user-detail";
    }
}
