package com.greenacademy.productstore.controller;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenacademy.productstore.models.Product;
import com.greenacademy.productstore.services.CategoryServices;
import com.greenacademy.productstore.services.ProductServices;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    private ProductServices productServices;
    private CategoryServices categoryServices;

    public ProductController(ProductServices productServices, CategoryServices categoryServices) {
        this.categoryServices = categoryServices;
        this.productServices = productServices;
    }

    @GetMapping("/products")
    public String index(Model model,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sku", required = false) String sku,
            @RequestParam(value = "sort_by", required = false) String sortBy) {
        

        System.out.println("name: " + name);
        System.out.println("sku: " + sku);
        System.out.println("sortBy: " + sortBy);

        Sort sort = Sort.by(Sort.Direction.DESC, "created_at");

        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy) {
                case "oldest":
                    sort = Sort.by(Sort.Direction.ASC, "created_at");
                    break;
                case "highest_price":
                    sort = Sort.by(Sort.Direction.DESC, "price");
                    break;
                case "lowest_price":
                    sort = Sort.by(Sort.Direction.ASC, "price");
                    break;
                case "highest_quantity":
                    sort = Sort.by(Sort.Direction.DESC, "quantity");
                    break;
                case "lowest_quantity":
                    sort = Sort.by(Sort.Direction.ASC, "quantity");
                    break;
                default:
                    break;
            }
        }
        model.addAttribute("products", productServices.getAll(name, sku, sort));

        model.addAttribute("name", name);
        model.addAttribute("sku", sku);
        model.addAttribute("sortBy", sort);
        return "pages/products/index";
    }

    @GetMapping("/products/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryServices.getAll());
        return "pages/products/create";
    }

    @PostMapping("/products")
    public String store(@Valid @ModelAttribute("product") Product product, BindingResult result, Model model) {

        if (result.hasErrors()) {
            model.addAttribute("errors", result);
            model.addAttribute("categories", categoryServices.getAll());
            return "pages/products/create";
        }

        productServices.create(product);
        return "redirect:/products";
    }

    @GetMapping("/products/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("product", productServices.getById(id));
        model.addAttribute("categories", categoryServices.getAll());
        return "pages/products/edit";
    }

    @PostMapping("/products/{id}")
    public String update(@Valid @PathVariable("id") Integer id, @ModelAttribute("product") Product product,
            BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result);
            return "pages/products/edit";
        }
        productServices.update(product);
        return "redirect:/products";
    }

    @PostMapping("/products/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        productServices.delete(id);
        return "redirect:/products";
    }

}
