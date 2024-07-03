package com.example.movietickets.demo.controller;

import com.example.movietickets.demo.model.Blog;
import com.example.movietickets.demo.model.Category;
import com.example.movietickets.demo.model.Comment;
import com.example.movietickets.demo.service.BlogService;
import com.example.movietickets.demo.service.CategoryService;
import com.example.movietickets.demo.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/blog")
public class BlogController {
    @Autowired
    private final BlogService blogService;
    private final CommentService commentService;
    private final CategoryService categoryService;

    // blog for user
    @GetMapping()
    public String listBlogUser(Model model,
                               @RequestParam(defaultValue = "0") Integer pageNo,
                               @RequestParam(defaultValue = "3") Integer pageSize,
                               @RequestParam(defaultValue = "id") String sortBy) {
        Page<Blog> page = blogService.getAllPostsForUser(pageNo, pageSize, sortBy);
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        List<Blog> blog = page.getContent();
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("blog", blog);
        return "/blog/blog-list";
    }

    @GetMapping("/blog-details/{id}")
    public String getBlogDetail(@PathVariable Long id, Model model) {
        Blog blog = blogService.findBlogWithId(id);
        List<Comment> comments = commentService.getAllCommentsByBlogId(id);
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("blog", blog);
        model.addAttribute("comments", comments);
        model.addAttribute("comment", new Comment());
        model.addAttribute("formattedDate", blog.getFormattedDate());

        return "blog/blog-details";
    }

    @GetMapping("/search")
    public String searchBlogs(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Blog> blogs = blogService.searchBlogsByTitle(keyword);
        model.addAttribute("blogs", blogs);
        model.addAttribute("keyword", keyword);
        return "blog/blog-search";
    }
}
