package com.greenacademy.productstore.controller;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.greenacademy.productstore.models.Product;
import com.greenacademy.productstore.models.Review;
import com.greenacademy.productstore.services.ProductServices;
import com.greenacademy.productstore.services.ReviewServices;

@Controller
public class FrontController {

    private ProductServices productServices;
    private ReviewServices reviewServices;

    public FrontController(ProductServices productServices, ReviewServices reviewServices) {
        this.productServices = productServices;
        this.reviewServices = reviewServices;
    }

    @GetMapping("/product/detail/{id}")
    public String detail(@PathVariable("id") Integer id, Model model, Pageable pageable) {

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        pageable = PageRequest.of(pageable.getPageNumber(), 10, sort);

        Product product = productServices.getById(id);
        Page<Review> reviews = reviewServices.getByProductId(id, pageable);

        Double rating = reviewServices.getAvarageByProductId(id);

        model.addAttribute("reviews", reviews);
        model.addAttribute("product", product);
        model.addAttribute("rating", rating);

        return "pages/front/details-product";
    }
    
}
