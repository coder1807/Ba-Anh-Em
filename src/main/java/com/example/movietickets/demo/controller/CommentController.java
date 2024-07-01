package com.example.movietickets.demo.controller;

import com.example.movietickets.demo.model.*;
import com.example.movietickets.demo.service.BlogService;
import com.example.movietickets.demo.service.CategoryService;
import com.example.movietickets.demo.service.CommentService;
import com.example.movietickets.demo.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
@RequestMapping("/blog")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private final BlogService blogService;

    @Autowired
    private final UserService userService;

    @Autowired
    private  final CategoryService categoryService;

//    @PostMapping("/blog-details/{id}/comment")
//    public String addComment(@PathVariable Long id, @Valid @ModelAttribute Comment comment,
//                             BindingResult result, Model model, RedirectAttributes redirectAttributes) {
//
//        // Kiểm tra lỗi binding
//        /*if (result.hasErrors()) {
//            Blog blog = blogService.findBlogWithId(id);
//            model.addAttribute("blog", blog);
//            model.addAttribute("categories", commentService.getAllCommentsByPostId(id));
//            return "blog/blog-detail";
//        }*/
//
//        // Lấy thông tin blog từ id
//        Blog blog = blogService.findBlogWithId(id);
//
//        // Kiểm tra và lấy thông tin người dùng đăng nhập hiện tại
//        User user = userService.getCurrentUser();
//
//        List<Category> categories = categoryService.getAllCategories();
//
//        model.addAttribute("categories", categories);
//
//        // Thiết lập thông tin cho comment
//        Comment newComment = new Comment();
//        newComment.setBlog(blog);
//        newComment.setUser(user);
//        newComment.setContent(comment.getContent());
//        newComment.setDate(LocalDateTime.now());
//
//        // Lưu comment vào cơ sở dữ liệu
//        commentService.saveComment(newComment);
//
//        // Điều hướng lại đến trang chi tiết blog
//        return "redirect:/blog/blog-details/" + id;
//    }


    @PostMapping("/blog-details/{id}/comment")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> addComment(@PathVariable Long id, @Valid @ModelAttribute Comment comment,
                                                          BindingResult result) {
        Map<String, Object> response = new HashMap<>();

        if (result.hasErrors()) {
            response.put("success", false);
            response.put("message", "Dữ liệu bình luận không hợp lệ");
            return ResponseEntity.badRequest().body(response);
        }

        // Lấy thông tin blog từ id
        Blog blog = blogService.findBlogWithId(id);
        if (blog == null) {
            response.put("success", false);
            response.put("message", "Không tìm thấy blog với id: " + id);
            return ResponseEntity.badRequest().body(response);
        }

        // Kiểm tra và lấy thông tin người dùng đăng nhập hiện tại
        User user = userService.getCurrentUser();
        if (user == null) {
            response.put("success", false);
            response.put("message", "Người dùng chưa đăng nhập");
            return ResponseEntity.badRequest().body(response);
        }

        // Thiết lập thông tin cho comment
        Comment newComment = new Comment();
        newComment.setBlog(blog);
        newComment.setUser(user);
        newComment.setContent(comment.getContent());
        newComment.setDate(LocalDateTime.now());

        // Lưu comment vào cơ sở dữ liệu
        commentService.saveComment(newComment);

        // Chuẩn bị dữ liệu JSON trả về
        Map<String, Object> commentData = new HashMap<>();
        commentData.put("id", newComment.getId());
        commentData.put("formattedDate", newComment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        commentData.put("content", newComment.getContent());

        Map<String, String> userData = new HashMap<>();
        userData.put("fullname", user.getFullname());
        commentData.put("user", userData);

        response.put("success", true);
        response.put("comment", commentData);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/blog-details/{id}/delete/{commentId}")
    public String deleteComment(@PathVariable("commentId") Long commentId, @PathVariable("id") Long blogId, RedirectAttributes redirectAttributes) {
        commentService.deleteComment(commentId);
        redirectAttributes.addFlashAttribute("message", "Comment đã được xóa thành công.");
        return "redirect:/blog/blog-details/" + blogId;
    }
}
