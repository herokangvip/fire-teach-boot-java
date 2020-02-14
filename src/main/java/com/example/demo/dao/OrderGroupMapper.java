package com.example.demo.dao;

import com.example.demo.domain.OrderGroup;
import com.example.demo.domain.OrderTotal;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderGroupMapper {
    int insert(OrderGroup record);

    List<OrderGroup> select(Integer orderDay);

}