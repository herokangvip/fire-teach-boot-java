package com.example.demo.domain.es;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.es.SpInfo;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class MySku {

    private String skuId;
    private String skuName;
    private String[] category;
    private Double price;

    private SpInfo spInfo;
    private String createTime;

    public MySku() {
    }

    public MySku(String skuId, String skuName, String[] category, Double price, SpInfo spInfo) {
        this.skuId = skuId;
        this.skuName = skuName;
        this.category = category;
        this.price = price;
        this.spInfo = spInfo;
    }

    public MySku(String skuId, String skuName, String[] category, Double price, SpInfo spInfo, String createTime) {
        this.skuId = skuId;
        this.skuName = skuName;
        this.category = category;
        this.price = price;
        this.spInfo = spInfo;
        this.createTime = createTime;
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        MySku mySku = new MySku();
        mySku.setSkuId("");
        mySku.setSkuName("");
        mySku.setCategory(new String[]{"手机"});
        mySku.setPrice(0.0D);
        mySku.setSpInfo(new SpInfo());
        mySku.setCreateTime(sdf.format(new Date()));
        System.out.println(JSONObject.toJSONString(mySku));
    }

}
