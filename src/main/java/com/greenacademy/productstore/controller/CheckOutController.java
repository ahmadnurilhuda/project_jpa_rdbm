package com.greenacademy.productstore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import com.greenacademy.productstore.models.Order;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.services.CartServices;
import com.greenacademy.productstore.services.OrderServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class CheckOutController {

    private CartServices cartServices;
    private OrderServices orderServices;

    public CheckOutController(CartServices cartServices, OrderServices orderServices) {
        this.cartServices = cartServices;
        this.orderServices = orderServices;
    }
    
    @PostMapping("/checkouts")
    public String checkout(HttpSession session) {
        User user = (User) session.getAttribute("user");
        orderServices.checkout(user);
        return "redirect:/carts";
    }
}
