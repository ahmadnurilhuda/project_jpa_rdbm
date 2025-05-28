package com.greenacademy.productstore.controller;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.greenacademy.productstore.models.OrderItem;
import com.greenacademy.productstore.models.Review;
import com.greenacademy.productstore.dto.OrderItemDTO;
import com.greenacademy.productstore.models.Order;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.services.OrderServices;
import com.greenacademy.productstore.services.ReviewServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class OrderHistoryController {

    private OrderServices orderServices;
    private ReviewServices reviewServices;

    public OrderHistoryController(OrderServices orderServices, ReviewServices reviewServices) {
        this.orderServices = orderServices;
        this.reviewServices = reviewServices;
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

        ArrayList<OrderItemDTO> orderItemDTOs = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            Boolean isReviewed = false;

            Optional<Review> review = reviewServices.getByOrderItemId(orderItem.getId());
            if(review.isPresent()){
                isReviewed = true;
            }
            orderItemDTOs.add(new OrderItemDTO(orderItem, isReviewed));
        }
        
        if (order.isEmpty() || order.get().getUser().getId() != user.getId()) {
            return "redirect:/order-history";
        }

        model.addAttribute("order", order.get());
        model.addAttribute("orderItems", orderItemDTOs);
        return "pages/orders/detail";
    }
}
