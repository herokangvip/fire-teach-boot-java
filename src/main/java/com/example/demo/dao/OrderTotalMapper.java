package com.example.demo.dao;

import com.example.demo.domain.OrderTotal;
import com.example.demo.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTotalMapper {
    int insert(OrderTotal record);

    OrderTotal select(Integer orderDay);

}