package com.example.demo.domain;

/**
 * 电商监控大屏销量与订单金额汇总数据
 */
public class OrderTotal {
    private Integer id;
    private Integer orderDay;
    private Long totalNum;
    private Double totalMoney;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderDay() {
        return orderDay;
    }

    public void setOrderDay(Integer orderDay) {
        this.orderDay = orderDay;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public String toString() {
        return "OrderTotal{" +
                "id=" + id +
                ", orderDay=" + orderDay +
                ", totalNum=" + totalNum +
                ", totalMoney=" + totalMoney +
                '}';
    }
}
