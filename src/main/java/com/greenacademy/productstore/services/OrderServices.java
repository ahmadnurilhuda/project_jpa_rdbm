package com.greenacademy.productstore.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.greenacademy.productstore.models.CartItem;
import com.greenacademy.productstore.models.Enums.Status;
import com.greenacademy.productstore.models.Order;
import com.greenacademy.productstore.models.OrderItem;
import com.greenacademy.productstore.models.Product;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.repositories.OrderItemRepository;
import com.greenacademy.productstore.repositories.OrderRepository;
import com.greenacademy.productstore.repositories.ProductRepository;

@Service
public class OrderServices {

    private CartServices cartServices;
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    private OrderItemRepository orderItemRepository;
    private ProductServices productServices;
    private VoucherServices voucherServices;

    public OrderServices(CartServices cartServices, OrderRepository orderRepository, OrderItemRepository orderItemRepository, ProductServices productServices, ProductRepository productRepository, VoucherServices voucherServices) {
        this.productServices = productServices;
        this.productRepository = productRepository;
        this.cartServices = cartServices;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.voucherServices = voucherServices;
    }

    public void checkout(User user, String voucherCode) {

        Iterable<CartItem> cartItems = cartServices.getAll(user);
        BigDecimal amount = cartServices.getTotal(user);

        BigDecimal discount = BigDecimal.ZERO;
        try {
            discount = voucherServices.calculateDiscount(voucherCode, amount);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        amount = amount.subtract(discount);

        Order order  = orderRepository.save(new Order(user, amount, Status.PENDING, voucherCode, discount));

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity());

            //update product quantity
            Product product = productServices.getById(cartItem.getProduct().getId());
            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
            productRepository.save(product);
            orderItemRepository.save(orderItem);
        }
        cartServices.clear(cartItems);
        voucherServices.useVoucher(voucherCode);
    }

    public Iterable<Order> getOrderByUser(User user) {
        return orderRepository.findByUserId(user.getId());
    }

    public Optional<Order> getById(Integer id) {
        return orderRepository.findById(id);
    }

    public Iterable<OrderItem> getOrderItems(Integer id) {
        return orderItemRepository.findByOrderId(id);
    }

    public Iterable<Order> getAll() {
        return orderRepository.findAll();
    }

    public void updateStatus(Integer id, String status) {
        Order order = orderRepository.findById(id).get();
        order.setStatus(Status.valueOf(status));
        orderRepository.save(order);
    }

}
