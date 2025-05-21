package com.greenacademy.productstore.services;

import java.time.Instant;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.greenacademy.productstore.models.Product;
import com.greenacademy.productstore.repositories.ProductRepository;

@Service
public class ProductServices {
    private ProductRepository productRepository;

    public ProductServices(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Iterable<Product> getAll(String name, String sku, Sort sort) {
        return productRepository.findAllByNameAndSku(name, sku, sort);
    }

    public Product getById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product create(Product newProduct) {

        productRepository.save(newProduct);

        // for(Product product : productRepository.findAll()) {
        //     if(product.getSku().equals(newProduct.getSku())) {
        //         return null;
        //     }
        //     productRepository.save(newProduct);
        // }
        return newProduct;
    }

    public Product update(Product product) {
        Product existingProduct = productRepository.findById(product.getId()).orElse(null);

        if(existingProduct != null) {
            existingProduct.setName(product.getName());
            existingProduct.setDescription(product.getDescription());

            if(!product.getSku().equals(product.getSku())) {
                existingProduct.setSku(product.getSku());
            }
            existingProduct.setSku(product.getSku());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setDiscount(product.getDiscount());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setQuantity(product.getQuantity());
            existingProduct.setUpdated_at(Instant.now());

            productRepository.save(existingProduct);
        }
        return existingProduct;
    }

    public void delete(Integer id) {
        productRepository.deleteById(id);
    }

    public Iterable<Product> getByCategory(Integer id) {
        return productRepository.findByCategoryId(id);
    }
}
