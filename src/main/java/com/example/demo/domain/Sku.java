package com.example.demo.domain;

import org.springframework.data.annotation.Id;
//import org.springframework.data.elasticsearch.annotations.Document;
//import org.springframework.data.elasticsearch.annotations.Field;
//import org.springframework.data.elasticsearch.annotations.FieldType;

//@Document 作用在类，标记实体类为文档对象，一般有两个属性
//        indexName：对应索引库名称
//        type：对应在索引库中的类型
//        shards：分片数量，默认5
//        replicas：副本数量，默认1
//@Document(indexName = "sku_index", type = "sku_type", shards = 1, replicas = 1)
public class Sku {
   /* @Id
    private Long id;

    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String name;//商品名称

    @Field(type = FieldType.Keyword)
    private String category;// 分类


    @Field(type = FieldType.Double)
    private Double price; // 价格

    public Sku() {
    }

    public Sku(Long id, String name, String category, Double price) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Sku{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                '}';
    }*/
}
