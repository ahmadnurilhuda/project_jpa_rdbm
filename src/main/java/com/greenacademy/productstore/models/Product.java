package com.greenacademy.productstore.models;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Description is required")
    private String description;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "SKU is required")
    private String sku;

    @Column(nullable = false, precision = 18, scale = 2)
    @NotNull(message = "Price is required")
    private BigDecimal price;

    @Column(nullable = false)
    @NotNull(message = "Discount is required")
    private Double discount;

    // semua product hanya ada 1 category, gunakan many to one
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "Category is required")
    private Category category;

    @Column(nullable = false)
    @NotNull(message = "Quantity is required")
    @PositiveOrZero(message = "Stock must be greater than zero")
    private Integer quantity;

    @CreationTimestamp
    private Instant created_at;

    @UpdateTimestamp
    private Instant updated_at;
    

    public Product() {
    }

    public Product(String name, String description, BigDecimal price, Double discount, Category category, Integer quantity, String sku) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.discount = discount;
        this.category = category;
        this.quantity = quantity;
        this.sku = sku;
        this.created_at = Instant.now();
        this.updated_at = Instant.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Instant getCreated_at() {
        return created_at;
    }

    // created_at tidak boleh diubah
    // public void setCreated_at(Instant created_at) {
    //     this.created_at = created_at;
    // }

    public Instant getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Instant updated_at) {
        this.updated_at = updated_at;
    }
}
