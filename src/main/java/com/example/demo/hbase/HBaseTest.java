package com.example.demo.hbase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Map;


public class HBaseTest {

    public static void main(String[] args) throws Exception {
        HBaseUtils.getData("kang","10001","","");
    }


}
