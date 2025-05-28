package com.greenacademy.productstore.dto;

import com.greenacademy.productstore.models.Product;

public class ProductDTO {

    private Product product;
    private Double rating;

    public ProductDTO(Product product, Double rating) {
        this.product = product;
        this.rating = rating;
    }

    public Product getProduct() {
        return product;
    }

    public Double getRating() {
        return rating;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }
}
