package com.example.demo.dao;

import com.example.demo.domain.Product;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

public interface ProductMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    @Update("update product set stock=stock-1 where product_id=#{productId}")
    void decrStockByProductId(@Param("productId") Long productId);

    @Update("update product set stock=stock+1 where product_id=#{productId}")
    void incrStockByProductId(@Param("productId") Long productId);
}