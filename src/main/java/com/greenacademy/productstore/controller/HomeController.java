package com.greenacademy.productstore.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.greenacademy.productstore.models.Product;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.services.ProductServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    ProductServices productServices;

    public HomeController(ProductServices productServices) {
        this.productServices = productServices;
    }

    @GetMapping("/")
    public String index(HttpSession session, Model model, Pageable pageable, Product product) {

        pageable = PageRequest.of(pageable.getPageNumber(), 10, pageable.getSort());
        // Page<Product> products = productServices.getAll(product.getName(), product.getSku(), pageable);
        PagedModel<Product> products = new PagedModel<>(productServices.getAll(product.getName(), product.getSku(), pageable));

        model.addAttribute("products", products);
        model.addAttribute("metadata", products.getMetadata());
        return "pages/home/index";
    }
}



