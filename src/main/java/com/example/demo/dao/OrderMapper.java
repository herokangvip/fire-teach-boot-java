package com.example.demo.dao;

import com.example.demo.domain.Order;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

public interface OrderMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Order record);

    int insertSelective(Order record);

    Order selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);

    @Delete("delete from `order` where order_id=#{orderId}")
    void deleteByOrderId(@Param("orderId") Long orderId);
}