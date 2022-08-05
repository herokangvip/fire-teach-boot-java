package com.example.demo;


import com.example.demo.config.zk.ZkUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
public class ZkTest {
    @Autowired
    private ZkUtils zkUtils;


    @Test
    public void test1() throws Exception {
        zkUtils.addWatcherWithChildCache("/");

        //String hello = zkUtils.createNode("/test", "hello", false, true);
        //String zookeeper = zkUtils.getNodeData("/zookeeper");
        System.out.println("-");
        Thread.sleep(Integer.MAX_VALUE);
    }
}
