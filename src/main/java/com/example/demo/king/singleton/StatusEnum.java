package com.example.demo.king.singleton;

public enum StatusEnum {
    START(1, "开始"),
    END(2, "结束");
    private int key;
    private String value;

    StatusEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }


    public String getValue() {
        return value;
    }


}
