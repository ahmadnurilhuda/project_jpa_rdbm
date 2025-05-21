package com.greenacademy.productstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.greenacademy.productstore.models.Category;
import com.greenacademy.productstore.services.CategoryServices;
import com.greenacademy.productstore.services.ProductServices;

import groovy.lang.Binding;
import jakarta.validation.Valid;

@Controller
public class CategoryController {
    private CategoryServices categoryServices;
    private ProductServices productServices;

    public CategoryController(CategoryServices categoryServices, ProductServices productServices) {
        this.categoryServices = categoryServices;
        this.productServices = productServices;
    }

    @GetMapping("/categories")
    public String index(Model model) {
        model.addAttribute("categories", categoryServices.getAll());
        return "pages/categories/index";
    }
    
    @GetMapping("/categories/create")
    public String create(Model model) {
        model.addAttribute("category", new Category(null, null));
        return "pages/categories/create";
    }

    @PostMapping("/categories")
    public String store(
        @Valid
        @ModelAttribute("category")Category category,
        BindingResult result,
        Model model) {

        if(result.hasErrors()) {
            model.addAttribute("errors", result);
            return "pages/categories/create";
        }

        categoryServices.create(category);
        return "redirect:/categories";
    }

    @GetMapping("/categories/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("category", categoryServices.getById(id));
        return "pages/categories/edit";
    }

    @PostMapping("/categories/edit/{id}")
    public String update(@Valid @PathVariable("id") Integer id, @ModelAttribute("category") Category category, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("errors", result);
            return "pages/categories/edit";
        }
        categoryServices.update(category);
        return "redirect:/categories";
    }

    @PostMapping("/categories/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        categoryServices.delete(id);
        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}")
    public String show(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("products", productServices.getByCategory(id));
        model.addAttribute("category", categoryServices.getById(id));
        return "pages/categories/show";
    }
}
