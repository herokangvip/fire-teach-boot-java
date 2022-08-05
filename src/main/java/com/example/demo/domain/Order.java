package com.example.demo.domain;

import java.io.Serializable;

public class Order implements Serializable {
    private Long id;

    private Long orderId;

    private Long productId;

    public Order() {
    }

    public Order(Long orderId, Long productId) {
        this.orderId = orderId;
        this.productId = productId;
    }

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}