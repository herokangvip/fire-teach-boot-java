package com.example.demo.config.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * Description: zk 工具类
 * 使用Curator 实现zk 的基本操作-增删查改数据 和监听watch
 */
@Slf4j
//@Service
public class ZkUtils {
    @Autowired
    private CuratorFramework client;

    public String createNode(String path, String value, boolean persistent, boolean sequential) throws Exception {
        if (null == client) {
            throw new RuntimeException("there is not connect to zkServer...");
        }
        CreateMode mode = null;
        if (persistent && sequential) {
            mode = CreateMode.PERSISTENT_SEQUENTIAL;//持久顺序节点
        }
        if (persistent && !sequential) {
            mode = CreateMode.PERSISTENT;
        }
        if (!persistent && !sequential) {
            mode = CreateMode.EPHEMERAL;
        }
        if (!persistent && sequential) {
            mode = CreateMode.EPHEMERAL_SEQUENTIAL;//临时顺序节点
        }
        String node = client
                .create()
                .creatingParentsIfNeeded()
                .withMode(mode)
                .forPath(path, value.getBytes());

        log.info("create node : {}", node);
        return node;
    }

    /**
     * @param path
     * @Description: 删除节点信息
     * @Date: 2020-08-22 16:01
     */
    public void deleteNode(String path) throws Exception {
        if (null == client) {
            throw new RuntimeException("there is not connect to zkServer...");
        }
        client.delete()
                .guaranteed() // 保障机制，若未删除成功，只要会话有效会在后台一直尝试删除
                .deletingChildrenIfNeeded() // 若当前节点包含子节点，子节点也删除
                .forPath(path);
        log.info("{} is deleted ", path);
    }

    /**
     * 判断znode是否存在，Stat就是对znode所有属性的一个映射，stat=null表示节点不存在
     *
     * @param path
     * @return
     */
    public Stat isExists(String path) throws Exception {
        if (null == client) {
            throw new RuntimeException("there is not connect to zkServer...");
        }
        return client.checkExists().forPath(path);
    }

    /**
     * 查询子节点
     *
     * @param path
     * @return
     * @throws Exception
     */
    public List<String> getChildren(String path) throws Exception {
        if (null == client) {
            throw new RuntimeException("there is not connect to zkServer...");
        }
        return client.getChildren()
                .forPath(path);
    }


    /**
     * @param path
     * @Description: 获取节点存储的值
     * @Date: 2020-08-22 16:11
     */
    public String getNodeData(String path) throws Exception {
        if (null == client) {
            throw new RuntimeException("there is not connect to zkServer...");
        }
        Stat stat = new Stat();
        byte[] bytes = client.getData().storingStatIn(stat).forPath(path);
        log.info("{} data is : {}", path, new String(bytes));
        log.info("current stat version is {}, createTime is {}", stat.getVersion(), stat.getCtime());
        return new String(bytes);
    }


    /**
     * @param path
     * @param value
     * @Description: 设置节点 数据
     * @Date: 2020-08-22 16:17
     */
    public void setNodeData(String path, String value) throws Exception {
        if (null == client) {
            throw new RuntimeException("there is not connect to zkServer...");
        }
        Stat stat = client.checkExists().forPath(path);
        if (null == stat) {
            log.info(String.format("{} Znode is not exists", path));
            throw new RuntimeException(String.format("{} Znode is not exists", path));
        }
        String nodeData = getNodeData(path);
        client.setData().withVersion(stat.getVersion()).forPath(path, value.getBytes());
        log.info("{} Znode data is set. old vaule is {}, new data is {}", path, nodeData, value);
    }


    /**
     * @param path
     * @Description: 创建 给定节点的监听事件  监听一个节点的更新和创建事件(不包括删除)
     * @Date: 2020-08-22 16:47
     */
    public void addWatcherWithNodeCache(String path) throws Exception {
        if (null == client) {
            throw new RuntimeException("there is not connect to zkServer...");
        }
        // dataIsCompressed if true, data in the path is compressed
        NodeCache nodeCache = new NodeCache(client, path, false);
        NodeCacheListener listener = () -> {
            ChildData currentData = nodeCache.getCurrentData();
            log.info("{} Znode data is chagnge,new data is ---  {}", currentData.getPath(), new String(currentData.getData()));
        };
        nodeCache.getListenable().addListener(listener);
        nodeCache.start();
    }


    /**
     * @param path 给定节点
     * @Description: 监听给定节点下的子节点的创建、删除、更新
     * @Date: 2020-08-22 17:14
     */
    public void addWatcherWithChildCache(String path) throws Exception {
        if (null == client) {
            throw new RuntimeException("there is not connect to zkServer...");
        }
        //cacheData if true, node contents are cached in addition to the stat
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, false);
        PathChildrenCacheListener listener = (client, event) -> {
            log.info("event path is --{} ,event type is {},date is {}", event.getData().getPath(), event.getType(),
                    event.getData());
        };
        pathChildrenCache.getListenable().addListener(listener);
        // StartMode : NORMAL  BUILD_INITIAL_CACHE  POST_INITIALIZED_EVENT
        // NORMAL:异步初始化, BUILD_INITIAL_CACHE:同步初始化, POST_INITIALIZED_EVENT:异步初始化,初始化之后会触发事件
        pathChildrenCache.start(PathChildrenCache.StartMode.NORMAL);
    }

    /**
     * @param path
     * @Description: 监听 给定节点的创建、更新（不包括删除） 以及 该节点下的子节点的创建、删除、更新动作。
     * @Date: 2020-08-22 17:35
     */
    public void addWatcherWithTreeCache(String path) throws Exception {
        if (null == client) {
            throw new RuntimeException("there is not connect to zkServer...");
        }
        TreeCache treeCache = new TreeCache(client, path);
        TreeCacheListener listener = (client, event) -> {
            log.info("节点路径 --{} ,节点事件类型: {} , 节点值为: {}", Objects.nonNull(event.getData()) ? event.getData().getPath() : "无数据", event.getType());
        };
        treeCache.getListenable().addListener(listener);
        treeCache.start();
    }

}



