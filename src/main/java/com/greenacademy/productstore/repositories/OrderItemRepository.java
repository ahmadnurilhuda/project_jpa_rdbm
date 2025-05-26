package com.greenacademy.productstore.repositories;

import org.springframework.data.repository.CrudRepository;

import com.greenacademy.productstore.models.OrderItem;

public interface OrderItemRepository extends CrudRepository<OrderItem, Integer> {
    Iterable <OrderItem> findByOrderId(Integer orderId);
    
}
