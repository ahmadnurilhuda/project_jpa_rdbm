package com.greenacademy.productstore.controller;

import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.greenacademy.productstore.models.OrderItem;
import com.greenacademy.productstore.models.Order;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.services.OrderServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderHistoryController {

    private OrderServices orderServices;

    public OrderHistoryController(OrderServices orderServices) {
        this.orderServices = orderServices;
    }

    @GetMapping("/order-history")
    public String history(HttpSession session,Model model,Pageable pageable) {
        
        Sort sort = Sort.by(Sort.Direction.DESC, "created_at");
        pageable = PageRequest.of(pageable.getPageNumber(),pageable.getPageSize(), sort);
        User user = (User) session.getAttribute("user");

        model.addAttribute("orders", orderServices.getOrderByUser(user));
        return "pages/orders/histories";
    }

    @GetMapping("/order-history/{id}")
    public String detail(@PathVariable("id") Integer id, HttpSession session,Model model) {
        User user = (User) session.getAttribute("user");

        Optional<Order> order = orderServices.getById(id);
        Iterable<OrderItem> orderItems = orderServices.getOrderItems(id);

        if (order.isEmpty() || order.get().getUser().getId() != user.getId()) {
            return "redirect:/order-history";
        }

        model.addAttribute("order", order.get());
        model.addAttribute("orderItems", orderItems);
        return "pages/orders/detail";
    }
}
