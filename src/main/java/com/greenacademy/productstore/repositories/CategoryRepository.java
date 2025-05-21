package com.greenacademy.productstore.repositories;

import org.springframework.data.repository.CrudRepository;

import com.greenacademy.productstore.models.Category;

public interface CategoryRepository extends CrudRepository<Category, Integer> {
     
}
