package com.greenacademy.productstore.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.greenacademy.productstore.models.CartItem;
import com.greenacademy.productstore.models.Product;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.repositories.CartItemsRepository;

@Service
public class CartServices {

    private CartItemsRepository cartItemsRepository;
    private ProductServices productServices;
    
    public CartServices(CartItemsRepository cartItemsRepository, ProductServices productServices) {
        this.cartItemsRepository = cartItemsRepository;
        this.productServices = productServices;
    }

    public Iterable<CartItem> getAll(User user) {
        return cartItemsRepository.findByUserId(user.getId());
    }

    public void add(Integer productId, User user) {

        Product product = productServices.getById(productId);
        CartItem existingCartItem = cartItemsRepository.findByProductIdAndUserId(productId, user.getId());

        if (existingCartItem != null) {
            existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
            cartItemsRepository.save(existingCartItem);
        } else {
            CartItem cartItem = new CartItem(product, user, 1);
            cartItemsRepository.save(cartItem);
        }
    }

    public BigDecimal getTotal(User user) {
        return cartItemsRepository.getTotalByUserId(user.getId());
    }

    public void remove(Integer id) {
        cartItemsRepository.deleteById(id);
    }

    public void clear(User user) {
        Iterable<CartItem> cartItems = cartItemsRepository.findByUserId(user.getId());
        for (CartItem cartItem : cartItems) {
            cartItemsRepository.deleteById(cartItem.getId());
        }
    }

    public void clear(Iterable<CartItem> cartItems) {
        for (CartItem cartItem : cartItems) {
            cartItemsRepository.deleteById(cartItem.getId());
        }
    }
    
}
