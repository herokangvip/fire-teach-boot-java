package com.example.demo.domain.vo;

public class OrderTotalVo {
    private String totalNum;
    private String totalMoney;

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    @Override
    public String toString() {
        return "OrderTotalVo{" +
                "totalNum='" + totalNum + '\'' +
                ", totalMoney='" + totalMoney + '\'' +
                '}';
    }
}
