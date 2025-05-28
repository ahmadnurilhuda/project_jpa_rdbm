package com.greenacademy.productstore.dto;

import com.greenacademy.productstore.models.OrderItem;

public class OrderItemDTO {

    private OrderItem orderItem;
    private Boolean isReviewed;

    public OrderItemDTO(OrderItem orderItem, Boolean isReviewed) {
        this.orderItem = orderItem;
        this.isReviewed = isReviewed;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public Boolean getIsReviewed() {
        return isReviewed;
    }

    public void setIsReviewed(Boolean isReviewed) {
        this.isReviewed = isReviewed;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }
}
