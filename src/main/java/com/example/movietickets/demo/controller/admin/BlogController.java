package com.example.movietickets.demo.controller.admin;

import com.example.movietickets.demo.model.*;
import com.example.movietickets.demo.service.BlogService;
import com.example.movietickets.demo.service.CategoryService;
import com.example.movietickets.demo.service.CommentService;
import com.example.movietickets.demo.service.CountryService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Controller
@AllArgsConstructor
public class BlogController {

    @Autowired
    private final BlogService blogService;

    @Autowired
    private final CommentService commentService;

    @Autowired
    private final CategoryService categoryService;

    @GetMapping("/admin/blog")
    public String listBlog(Model model) {
        List<Blog> blog = blogService.getAllPosts();
        model.addAttribute("blog", blog);
        model.addAttribute("title", "Danh sách blog ");
        return "/admin/blog/blog-list";
    }


    // blog for user
    @GetMapping("/blog")
    public String listBlogUser(Model model) {
        List<Blog> blog = blogService.getAllPosts();
        List<Category> categories = categoryService.getAllCategories();

        model.addAttribute("categories", categories);
        model.addAttribute("blog", blog);
        return "/blog/blog-list";
    }

    @GetMapping("/blog/blog-details/{id}")
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

    @GetMapping("/blog/search")
    public String searchBlogs(Model model, @RequestParam(value = "keyword", required = false) String keyword) {
        List<Blog> blogs = blogService.searchBlogsByTitle(keyword);
        model.addAttribute("blogs", blogs);
        model.addAttribute("keyword", keyword);
        return "blog/blog-search";
    }

    // end blog for user

    //add blog
    @GetMapping("/admin/blog/add")
    public String showAddBlog(Model model){
        model.addAttribute("blog", new Blog());
        return "/admin/blog/blog-add";
    }

    @PostMapping("/admin/blog/add")
    public String addBlog(@Valid @ModelAttribute Blog blog, BindingResult result, @RequestParam("poster") MultipartFile poster) throws IOException {

      /*  if (result.hasErrors()) {
            return "/admin/blog/blog-add";
        }*/

        if (!poster.isEmpty()) {
            try {
                String imageName = saveImageStatic(poster);
                blog.setPoster("/assets/img/adminblog/" + imageName); //lưu đường dẫn vào database
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        blog.setDate(LocalDateTime.now());

        blogService.create(blog);
        return "redirect:/admin/blog";
    }

    private String saveImageStatic(MultipartFile image) throws IOException {
        Path dirImages = Paths.get("target/classes/static/assets/img/adminblog");
        if (!Files.exists(dirImages)) {
            Files.createDirectories(dirImages);
        }

        String newFileName = UUID.randomUUID()+ "." + StringUtils.getFilenameExtension(image.getOriginalFilename());

        Path pathFileUpload = dirImages.resolve(newFileName);
        Files.copy(image.getInputStream(), pathFileUpload,
                StandardCopyOption.REPLACE_EXISTING);
        return newFileName;
    }

    // Hiển thị form sửa blog
    @GetMapping("/admin/blog/edit/{id}")
    public String showEditBlogForm(@PathVariable("id") Long id, Model model) {
        Blog blog = blogService.getPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid blog Id: " + id));
        model.addAttribute("blog", blog);
        return "/admin/blog/blog-update";
    }

    // Cập nhật thông tin combofood
    @PostMapping("/admin/blog/edit/{id}")
    public String updateBlog(@PathVariable("id") Long id, @Valid @ModelAttribute Blog blog, BindingResult result, @RequestParam("poster") MultipartFile poster, Model model) throws IOException {
       /* if (result.hasErrors()) {
            return "/admin/combofood/food-update";
        }*/

        Blog existingBlog = blogService.getPostById(id).orElseThrow(() -> new IllegalArgumentException("Invalid film Id:" + id));

        if (!poster.isEmpty()) {
            String imageName = saveImageStatic(poster);
            existingBlog.setPoster("/assets/img/adminblog/" + imageName);
        }

        existingBlog.setTitle(blog.getTitle());
        existingBlog.setContent(blog.getContent());

        blogService.updateBlog(existingBlog);
        return "redirect:/admin/blog";
    }

    // Xóa blog
    @GetMapping("/admin/blog/delete/{id}")
    public String deleteBlog(@PathVariable("id") Long id) {
        blogService.deleteBlog(id);
        return "redirect:/admin/blog";
    }
}
