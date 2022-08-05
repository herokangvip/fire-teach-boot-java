package com.example.demo.king.saga;

public class SagaException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SagaException(String e){
        super(e);
    }
    public SagaException(Throwable e){
        super(e);
    }
}
