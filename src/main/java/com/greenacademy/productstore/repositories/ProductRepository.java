package com.greenacademy.productstore.repositories;

import org.springframework.data.repository.CrudRepository;

import com.greenacademy.productstore.models.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {

    
    
}
