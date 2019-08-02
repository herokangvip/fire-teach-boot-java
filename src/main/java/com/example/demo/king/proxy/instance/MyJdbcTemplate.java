package com.example.demo.king.proxy.instance;

import com.example.demo.king.proxy.Connection;
import com.example.demo.king.proxy.proxy.InvocationHandler;
import java.lang.Object;
import java.lang.Override;
import java.lang.reflect.Method;

public class MyJdbcTemplate implements Connection {
  private InvocationHandler invocationHandler;

  public MyJdbcTemplate(InvocationHandler invocationHandler) {
    this.invocationHandler = invocationHandler;
  }

  @Override
  public void add(Object param) {
    try {
    	Method method = Connection.class.getMethod("add",Object.class);
    	this.invocationHandler.invoke(method, param);
    } catch(Exception e) {
    	e.printStackTrace();
    }
  }

  @Override
  public void update(Object param) {
    try {
    	Method method = Connection.class.getMethod("update",Object.class);
    	this.invocationHandler.invoke(method, param);
    } catch(Exception e) {
    	e.printStackTrace();
    }
  }
}
