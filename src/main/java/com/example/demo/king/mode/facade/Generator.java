package com.example.demo.king.mode.facade;

public class Generator {
    public static void generat(){
        //生成controller代码
        ControllerGenerator controllerGenerator = new ControllerGenerator();
        controllerGenerator.generator();
        //生成service
        ServiceGenerator serviceGenerator = new ServiceGenerator();
        serviceGenerator.generator();
        //生成dao
        DaoGenerator daoGenerator = new DaoGenerator();
        daoGenerator.generator();
    }
}
