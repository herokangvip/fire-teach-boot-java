package com.example.demo.domain;

/**
 * 电商监控大屏，按省份聚合的销量和订单金额汇总数据
 */
public class OrderGroup {
    private Integer id;
    private String province;
    private Integer orderDay;
    private Long totalNum;
    private Double totalMoney;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
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
        return "OrderGroup{" +
                "id=" + id +
                ", province='" + province + '\'' +
                ", orderDay=" + orderDay +
                ", totalNum=" + totalNum +
                ", totalMoney=" + totalMoney +
                '}';
    }
}
