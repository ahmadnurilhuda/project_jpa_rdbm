package com.greenacademy.productstore.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.greenacademy.productstore.models.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {

   Iterable<Order> findByUserId(Integer userId);
   Optional<Order> findById(Integer id);
}
