package com.greenacademy.productstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.greenacademy.productstore.models.Product;
import com.greenacademy.productstore.models.Review;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.services.ReviewServices;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class ReviewController {

    private ReviewServices reviewServices;

    public ReviewController(ReviewServices reviewServices) {
        this.reviewServices = reviewServices;
    }

    @PostMapping("/reviews")
    public RedirectView store(@ModelAttribute("review") Review review, HttpSession session,
            HttpServletRequest request) {

        String referer = request.getHeader("Referer");
        System.out.println("\nReferer: " + referer + "\n");

        System.out.println("\nProduct ID yang dikirim: " + review.getProduct().getId());
        System.out.println("\nReview product ID: " + review.getProduct().getId());

        User user = (User) session.getAttribute("user");

        reviewServices.create(review, user);

        return new RedirectView(referer != null ? referer : "/");
    }

}
