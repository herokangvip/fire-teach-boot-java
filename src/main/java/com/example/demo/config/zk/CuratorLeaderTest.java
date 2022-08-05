package com.example.demo.config.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.EnsurePath;

public class CuratorLeaderTest {
    /**
     * Zookeeper info
     */
    private static final String ZK_ADDRESS = "127.0.0.1:2181";
    private static final String ZK_PATH = "/leaderTest";

    public static void main(String[] args) throws InterruptedException {
        LeaderSelectorListener listener = new LeaderSelectorListener() {
            @Override
            public void takeLeadership(CuratorFramework client) throws Exception {
                String env_name = System.getenv("env_name");
                System.out.println("env_name:"+env_name + " ===take leadership!");
                // takeLeadership() method should only return when leadership is being relinquished.
                Thread.sleep(Integer.MAX_VALUE);
                System.out.println(Thread.currentThread().getName() + " relinquish leadership!");
            }

            @Override
            public void stateChanged(CuratorFramework client, ConnectionState state) {
            }
        };
        Thread thread1 = new Thread(() -> {
            registerListener(listener);
        });
        thread1.start();
        Thread.sleep(Integer.MAX_VALUE);
    }

    private static void registerListener(LeaderSelectorListener listener) {
        // 1.Connect to zk
        CuratorFramework client = CuratorFrameworkFactory.newClient(
                ZK_ADDRESS,
                new RetryNTimes(2, 5000)
        );
        client.start();
        // 2.Ensure path
        //上层业务希望对一个数据节点进行一些操作，但是操作之前需要确保该节点存在。如果不存在则自动创建
        try {
            new EnsurePath(ZK_PATH).ensure(client.getZookeeperClient());
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 3.Register listener
        LeaderSelector selector = new LeaderSelector(client, ZK_PATH, listener);
        selector.autoRequeue();
        selector.start();
    }
}
