package com.greenacademy.productstore.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam(value = "sort", required = false) String sort,
            Pageable pageable) {

        System.out.println("name: " + name);
        System.out.println("sku: " + sku);
        System.out.println("sort: " + sort);

        pageable = PageRequest.of(pageable.getPageNumber(), 10, pageable.getSort());
        PagedModel<Product> products = new PagedModel<>(productServices.getAll(name, sku, pageable));

        model.addAttribute("products", products);
        model.addAttribute("metadata", products.getMetadata());
        model.addAttribute("name", name == null ? "" : name);
        model.addAttribute("sku", sku == null ? "" : sku);
        model.addAttribute("sort", sort == null ? "" : sort);
        return "pages/products/index";
    }

    @GetMapping("/products/create")
    public String create(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryServices.getAll());
        return "pages/products/create";
    }

    @PostMapping("/products")
    public String store(@Valid @ModelAttribute("product") Product product, BindingResult result, Model model,
            @RequestParam("image") MultipartFile productImage) {

        if (result.hasErrors()) {
            model.addAttribute("errors", result);
            model.addAttribute("categories", categoryServices.getAll());
            return "pages/products/create";
        }
        productServices.create(product, productImage);
        return "redirect:/products";
    }

    // save image to folde

    @GetMapping("/products/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("product", productServices.getById(id));
        model.addAttribute("categories", categoryServices.getAll());
        return "pages/products/edit";
    }

    @PostMapping("/products/{id}")
    public String update(@Valid @PathVariable("id") Integer id, @ModelAttribute("product") Product product,
            BindingResult result, Model model, @RequestParam("image") MultipartFile productImage) {
        if (result.hasErrors()) {
            model.addAttribute("errors", result);
            return "pages/products/edit";
        }
        productServices.update(product, productImage);
        return "redirect:/products";
    }

    @PostMapping("/products/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        Product product = productServices.getById(id);
        productServices.delete(product);
        return "redirect:/products";
    }

}
