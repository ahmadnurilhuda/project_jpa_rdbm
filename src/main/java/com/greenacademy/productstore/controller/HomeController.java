package com.greenacademy.productstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
    public String index(HttpSession session, Model model) {

        User user = (User) session.getAttribute("user");
        if(user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "pages/home/index";
    }
}



