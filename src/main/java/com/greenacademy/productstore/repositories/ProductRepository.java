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

    // use coalesce
   @Query("""
            SELECT new com.greenacademy.productstore.dto.ProductDTO(
                p,
                COALESCE(AVG(r.rating), 0.0),
                COUNT(DISTINCT r.id),
                COUNT(DISTINCT oi.id)
            )
            FROM Product p
            LEFT JOIN Review r ON p.id = r.product.id
            LEFT JOIN OrderItem oi ON p.id = oi.product.id
            LEFT JOIN Order o ON oi.order.id = o.id AND o.status = 'COMPLETED'
            GROUP BY p.id
            """)
Page<ProductDTO> findAll(Pageable pageable);


    @Query("SELECT p FROM Product p WHERE (:name IS NULL OR p.name LIKE %:name%) AND (:sku IS NULL OR p.sku LIKE %:sku%)")
    Page<Product> findAllByNameAndSku(@Param("name") String name, @Param("sku") String sku, Pageable pageable);

}
