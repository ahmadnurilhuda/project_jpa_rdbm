package com.greenacademy.productstore.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.greenacademy.productstore.dto.ProductDTO;
import com.greenacademy.productstore.models.Product;

public interface ProductRepository extends CrudRepository<Product, Integer> {


Iterable<Product> findByCategoryId(Integer id);

//use coalesce
@Query("SELECT new com.greenacademy.productstore.dto.ProductDTO(p, coalesce(AVG(r.rating), 0.0)) FROM Product p LEFT JOIN Review r ON p.id = r.product.id GROUP BY p.id")
Page<ProductDTO> findAll(Pageable pageable);
 


@Query("SELECT p FROM Product p WHERE (:name IS NULL OR p.name LIKE %:name%) AND (:sku IS NULL OR p.sku LIKE %:sku%)")
Page<Product> findAllByNameAndSku(@Param("name") String name, @Param("sku") String sku, Pageable pageable);
}
