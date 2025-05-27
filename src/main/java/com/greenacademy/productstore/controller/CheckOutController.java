package com.greenacademy.productstore.controller;

import java.math.BigDecimal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.greenacademy.productstore.models.CartItem;
import com.greenacademy.productstore.models.Order;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.services.CartServices;
import com.greenacademy.productstore.services.OrderServices;
import com.greenacademy.productstore.services.VoucherServices;

import jakarta.servlet.http.HttpSession;

@Controller
public class CheckOutController {

    private CartServices cartServices;
    private OrderServices orderServices;
    private VoucherServices voucherServices;

    public CheckOutController(CartServices cartServices, OrderServices orderServices, VoucherServices voucherServices) {
        this.cartServices = cartServices;
        this.orderServices = orderServices;
        this.voucherServices = voucherServices;
    }

    @GetMapping("/checkouts")
    public String index(@RequestParam(name = "voucherCode", required = false) String voucherCode, Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");

        Iterable<CartItem> cartItems = cartServices.getAll(user);
        BigDecimal amount = cartServices.getTotal(user);

        BigDecimal discount = BigDecimal.ZERO;
        try {
            discount = voucherServices.calculateDiscount(voucherCode, amount);
            model.addAttribute("voucherCode", voucherCode);
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }

        model.addAttribute("discount", discount);
        model.addAttribute("amount", amount == null ? BigDecimal.ZERO : amount);
        model.addAttribute("cartItems", cartItems);

        return "pages/checkouts/index";
    }
    
    @PostMapping("/checkouts")
    public String checkout(@RequestParam(name = "voucherCode", required = false) String voucherCode, HttpSession session) {
        User user = (User) session.getAttribute("user");
        orderServices.checkout(user, voucherCode);
        return "redirect:/carts";
    }
}
