package com.greenacademy.productstore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.greenacademy.productstore.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
     
    Page<Category> findAll(Pageable pageable);
}
