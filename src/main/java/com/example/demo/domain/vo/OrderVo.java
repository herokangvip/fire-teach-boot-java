package com.example.demo.domain.vo;

import java.io.Serializable;

public class OrderVo implements Serializable {
    private OrderTotalVo orderTotalVo;
    private String orderGroupVo;

    public OrderTotalVo getOrderTotalVo() {
        return orderTotalVo;
    }

    public void setOrderTotalVo(OrderTotalVo orderTotalVo) {
        this.orderTotalVo = orderTotalVo;
    }

    public String getOrderGroupVo() {
        return orderGroupVo;
    }

    public void setOrderGroupVo(String orderGroupVo) {
        this.orderGroupVo = orderGroupVo;
    }

    @Override
    public String toString() {
        return "OrderVo{" +
                "orderTotalVo=" + orderTotalVo +
                ", orderGroupVo='" + orderGroupVo + '\'' +
                '}';
    }
}
