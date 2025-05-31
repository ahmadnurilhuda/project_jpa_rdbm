package com.greenacademy.productstore.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.greenacademy.productstore.dto.ProductDTO;
import com.greenacademy.productstore.models.Product;
import com.greenacademy.productstore.services.EmailServices;
import com.greenacademy.productstore.services.ProductServices;

import jakarta.validation.constraints.Email;


@Controller
public class HomeController {

    private ProductServices productServices;
    private EmailServices emailServices;


    public HomeController(ProductServices productServices, EmailServices emailServices) {
        this.productServices = productServices;
        this.emailServices = emailServices;
    }

    @GetMapping("/")
    public String index(Model model, Pageable pageable, Product product) {

        pageable = PageRequest.of(pageable.getPageNumber(), 10, pageable.getSort());

        Page<ProductDTO> productDTOs = productServices.getAll(pageable);
        PagedModel<ProductDTO> products = new PagedModel<>(productDTOs);

        model.addAttribute("products", products);
        model.addAttribute("metadata", products.getMetadata());


        return "pages/home/index";
    }
}



