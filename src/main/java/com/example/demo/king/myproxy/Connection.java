package com.example.demo.king.myproxy;

public interface Connection {
    void add(Object object);
    void update(Object object);

    void delete(Long id);
    void find(Long id);
}
