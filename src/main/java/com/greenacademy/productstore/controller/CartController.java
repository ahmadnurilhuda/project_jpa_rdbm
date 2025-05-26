package com.greenacademy.productstore.controller;

import java.math.BigDecimal;
import java.rmi.dgc.DGC;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenacademy.productstore.models.CartItem;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.services.CartServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class CartController {

    private CartServices cartServices;

    public CartController(CartServices cartServices) {
        this.cartServices = cartServices;
    }

    @GetMapping("/carts")
    public String index(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

        Iterable cartItems = cartServices.getAll(user);

        BigDecimal total = cartServices.getTotal(user);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total == null ? BigDecimal.ZERO : total);

        return "pages/carts/index";
    }

    @PostMapping("/carts/add")
    public String add(@RequestParam("productId") Integer productId, HttpSession session) {

        User user = (User) session.getAttribute("user");
        cartServices.add(productId, user);

        return "redirect:/carts";
    }

    @PostMapping("/carts/remove")
    public String remove(@RequestParam("cartItemId") Integer id) {

        cartServices.remove(id);
        return "redirect:/carts";
    }
    
}
