package com.greenacademy.productstore.dto;



import com.greenacademy.productstore.models.Order;
import com.greenacademy.productstore.models.OrderItem;
import com.greenacademy.productstore.models.Product;
import com.greenacademy.productstore.models.Review;

public class ProductDTO {

    private Product product;
    private Double rating;
    private Long reviewCount;
    private Long soldCount;

    public ProductDTO(Product product, Double rating, Long reviewCount, Long soldCount) {
        this.product = product;
        this.rating = rating;
        this.reviewCount = reviewCount;
        this.soldCount = soldCount;
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

    public Long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Long reviewCount) {
        this.reviewCount = reviewCount;
    }

    public Long getSoldCount() {
        return soldCount;
    }

    public void setSoldCount(Long soldCount) {
        this.soldCount = soldCount;
    }
}
