package com.greenacademy.productstore.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenacademy.productstore.models.Order;
import com.greenacademy.productstore.models.OrderItem;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.services.OrderServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderController {

    private OrderServices orderServices;
    
    public OrderController (OrderServices orderServices) {
        this.orderServices = orderServices;
    }

    @GetMapping("/admin/orders")
    public String index(Model model) {

        model.addAttribute("orders", orderServices.getAll());
        return "pages/orders/index";
    }

    @GetMapping("/admin/orders/{id}")
    public String detail(@PathVariable("id") Integer id, Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        Optional<Order> order = orderServices.getById(id);
        Iterable<OrderItem> orderItems = orderServices.getOrderItems(id);
        String status = user.getRole().toString();

        if(order.isEmpty() || !status.equals("ADMIN")) {
            return "redirect:/admin/orders";
        }

        model.addAttribute("order",order.get());
        model.addAttribute("orderItems",orderItems);
        return "pages/orders/detail-admin";
    }

    @PostMapping("/admin/orders/update-status")
    public String updateStatus(@RequestParam("orderId") Integer id, @RequestParam("status") String status) {
        orderServices.updateStatus(id, status);
        return "redirect:/admin/orders";
    }





    
}
