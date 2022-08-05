package com.example.demo.config.zk;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.net.InetAddress.*;

public class LeaderLatchTest {

    private static Logger logger = LoggerFactory.getLogger(LeaderLatchTest.class);

    static int CLINET_COUNT = 1;
    static String LOCK_PATH = "/leader_latch";

    public static void main(String[] args) throws Exception {

        CuratorFramework client = getZkClient();
        LeaderLatch leaderLatch = new LeaderLatch(client, LOCK_PATH, "CLIENT_" + System.getenv("env_name"));
        //必须调用start()方法来进行抢主
        leaderLatch.start();
        leaderLatch.addListener(new LeaderLatchListener() {
            @Override
            public void isLeader() {
                System.out.println("我是leader:" + leaderLatch.getId());
            }
            @Override
            public void notLeader() {
                System.out.println("我不是leader，当前leader是：" + leaderLatch.getId());
            }
        });

        Thread.sleep(10000);
        /*if (leaderLatch.hasLeadership()) {
            System.out.println("当前leader:" + leaderLatch.getId());
            //释放leader权限 重新进行抢主
        }*/
        Thread.sleep(Integer.MAX_VALUE);

    }

    private static void checkLeader(List<LeaderLatch> leaderLatchList) throws Exception {
        //Leader选举需要时间 等待10秒
        //LeaderSelectorListenerAdapter adapter = null;
        Thread.sleep(10000);
        for (int i = 0; i < leaderLatchList.size(); i++) {
            LeaderLatch leaderLatch = leaderLatchList.get(i);
            //通过hasLeadership()方法判断当前节点是否是leader
            if (leaderLatch.hasLeadership()) {
                System.out.println("当前leader:" + leaderLatch.getId());
                //释放leader权限 重新进行抢主
                leaderLatch.close();
                checkLeader(leaderLatchList);
            }
        }
    }

    private static CuratorFramework getZkClient() {
        String zkServerAddress = "127.0.0.1:2181";
        ExponentialBackoffRetry retryPolicy = new ExponentialBackoffRetry(
                1000, 3, 5000);
        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
                .connectString(zkServerAddress)
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        return zkClient;
    }


}
