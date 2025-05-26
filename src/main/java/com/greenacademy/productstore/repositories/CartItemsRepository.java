package com.greenacademy.productstore.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.greenacademy.productstore.models.CartItem;

public interface CartItemsRepository extends CrudRepository<CartItem, Integer> {
    
    CartItem findByProductIdAndUserId(Integer productId, Integer userId);

    Iterable<CartItem> findByUserId(Integer userId);


    @Query(value = "SELECT SUM(c.product.price * c.quantity) FROM CartItem c WHERE c.user.id = :userId")
    BigDecimal getTotalByUserId(@Param("userId") Integer userId);

}
