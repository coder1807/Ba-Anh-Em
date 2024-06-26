package com.example.movietickets.demo.controller.admin;

import com.example.movietickets.demo.model.Category;
import com.example.movietickets.demo.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
@Controller
@RequiredArgsConstructor
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;


    //gửi response ra view add
    @GetMapping("/admin/categories/add")
    public String showAddForm(Model model) {
        model.addAttribute("category", new Category());
        return "/admin/category/category-add";
    }
    //gọi phương thức mapp tới form add
    @PostMapping("/admin/categories/add")
    public String addCategory(@Valid Category category, BindingResult result) {
        if (result.hasErrors()) {
            return "/admin/category/category-add";
        }
        categoryService.addCategory(category);
        return "redirect:/admin/categories";
    }
    // Hiển thị danh sách danh mục
    @GetMapping("/admin/categories")
    public String listCategories(Model model) {
        List<Category> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        model.addAttribute("title", "Danh sách thể loại");
        return "/admin/category/category-list";
    }
    // GET request to show category edit form
    @GetMapping("/admin/categories/edit/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid category Id:" + id));
        model.addAttribute("category", category);
        return "/admin/category/category-edit";
    }

    // POST request to update category
    @PostMapping("/admin/categories/edit/{id}")
    public String updateCategory(@PathVariable("id") Long id, @Valid Category category, BindingResult result, Model model) {
        if (result.hasErrors()) {
            category.setId(id);
            return "/admin/category/category-edit";
        }

        categoryService.updateCategory(category);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/admin/categories";
    }

    // GET request for deleting category
    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable("id") Long id, Model model) {
        Category category = categoryService.getCategoryById(id)
                .orElseThrow(() -> new IllegalArgumentException("Thể loại Id:" + id));

        categoryService.deleteCategoryById(id);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "redirect:/admin/categories";
    }
}
