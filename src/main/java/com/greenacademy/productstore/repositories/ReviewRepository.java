package com.greenacademy.productstore.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.greenacademy.productstore.models.Review;

public interface ReviewRepository extends CrudRepository<Review, Integer> {

    Page<Review> findByProductId(Integer id, Pageable pageable);    
    Optional<Review> findByOrderItemId(Integer id);

    @Query("SELECT COALESCE(AVG(r.rating), 0) FROM Review r WHERE r.product.id = :id")
    Double findAvarageByProductId(Integer id);
}
