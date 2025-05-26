package com.greenacademy.productstore.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.greenacademy.productstore.models.CartItem;
import com.greenacademy.productstore.models.Enums.Status;
import com.greenacademy.productstore.models.Order;
import com.greenacademy.productstore.models.OrderItem;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.repositories.OrderItemRepository;
import com.greenacademy.productstore.repositories.OrderRepository;

@Service
public class OrderServices {

    private CartServices cartServices;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;

    public OrderServices(CartServices cartServices, OrderRepository orderRepository, OrderItemRepository orderItemRepository) {
        this.cartServices = cartServices;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public void checkout(User user) {

        Iterable<CartItem> cartItems = cartServices.getAll(user);

        BigDecimal amount = cartServices.getTotal(user);

        Order order  = orderRepository.save(new Order(user, amount, Status.PENDING));

        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(order, cartItem.getProduct(), cartItem.getQuantity());
            orderItemRepository.save(orderItem);
        }
        cartServices.clear(cartItems);
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

}
