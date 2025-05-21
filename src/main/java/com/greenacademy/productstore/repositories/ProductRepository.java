package com.greenacademy.productstore.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.greenacademy.productstore.models.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {


Iterable<Product> findByCategoryId(Integer id);    


@Query("SELECT p FROM Product p WHERE (:name IS NULL OR p.name LIKE %:name%) AND (:sku IS NULL OR p.sku LIKE %:sku%)")
Iterable<Product> findAllByNameAndSku(@Param("name") String name, @Param("sku") String sku, Sort sort);
}
