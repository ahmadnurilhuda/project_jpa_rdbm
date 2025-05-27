package com.greenacademy.productstore.models;

import java.math.BigDecimal;
import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.greenacademy.productstore.models.Enums.DiscountType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "vouchers")
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "discount_type")
    private DiscountType discountType;

    @Column(nullable = false)
    private BigDecimal discount;


    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "min_purchase")
    private BigDecimal minPurchase = BigDecimal.ZERO;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;


    public Voucher() {
    }
    public Voucher(String name, String code, DiscountType discountType, BigDecimal discount, Integer quantity, BigDecimal minPurchase) {
        this.name = name;
        this.code = code;
        this.discountType = discountType;
        this.discount = discount;
        this.quantity = quantity;
        this.minPurchase = minPurchase;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
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
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public DiscountType getDiscountType() {
        return discountType;
    }
    public void setDiscountType(DiscountType discountType) {
        this.discountType = discountType;
    }
    public BigDecimal getDiscount() {
        return discount;
    }
    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }
    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
    public Instant getCreatedAt() {
        return createdAt;
    }
    public Instant getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdated_at(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
    public BigDecimal getMinPurchase() {
        return minPurchase;
    }
    public void setMinPurchase(BigDecimal minPurchase) {
        this.minPurchase = minPurchase;
    }
   
}
