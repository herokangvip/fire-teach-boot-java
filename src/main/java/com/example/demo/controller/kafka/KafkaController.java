package com.example.demo.controller.kafka;

import com.example.demo.config.ApplicationContextHolder;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 对外提供http接口，可以从指定offset开始消费，直接提交offset即可，方法2为测试先取消订阅再重新订阅的功能
 */
//@Controller
//@RequestMapping("/kafka")
public class KafkaController {
    @Value("${promotion.kafka.consumer.resetToTime:05:00:00}")
    private String resetToTime;

    @PostConstruct
    public void init() {
        System.out.println("kkkkk:" + resetToTime);
    }

    private static KafkaConsumer<String, String> consumer = null;
    private static AtomicBoolean running = new AtomicBoolean(true);
    private static Properties props = new Properties();
    private static Collection<String> topics = Collections.singletonList("testoffset");
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();


    //重置开关
    private static AtomicBoolean resetOffsetSwitch = new AtomicBoolean(false);
    //消费暂停状态
    private static AtomicBoolean consumerWaitingState = new AtomicBoolean(false);


    private static ReentrantLock consumerLock = new ReentrantLock(true);
    private static Condition condition = consumerLock.newCondition();

    /*static {

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");//
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "miner"); //消费者组
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); //自动提交
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1"); //每次最多拉取1条
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "10000"); //两次pool最大时间间隔
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(topics, new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                System.out.println("====rebalance前");
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                System.out.println("====rebalance后");
                for (TopicPartition topicPartition : collection) {
                    //指定offset拉取
                    consumer.seekToBeginning(Collections.singletonList(new TopicPartition("testoffset", 0)));
                }
            }
        });
        executorService.submit(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                while (running.get()) {

                    //没有分到分区的消费者record size=0
                    boolean tryLock = consumerLock.tryLock();
                    if (tryLock) {
                        try {
                            boolean resetOffsetSwitchState = resetOffsetSwitch.get();
                            if (resetOffsetSwitchState) {
                                System.out.println("执行暂停消费");
                                consumerWaitingState.set(true);
                                condition.await();
                            }
                            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(3)); //拉取数据
                            if (records != null && !records.isEmpty()) {
                                for (ConsumerRecord<String, String> record : records) {
                                    System.out.printf("====:partition = %s,offset = %d, key = %s, value= %s%n", record.partition(), record.offset(), record.key(), record.value());
                                }
                            }
                            Thread.sleep(1000);
                            if (!running.get()) {
                                break;
                            }
                        } finally {
                            consumerLock.unlock();
                        }
                    }
                }
            }
        });
    }*/


    @RequestMapping("/test")
    @ResponseBody
    public String test() throws InterruptedException {
        //PromotionJdqConsumer.resetOffsetSwitch.set(true);
        Atest bean = ApplicationContextHolder.getBean(Atest.class);
        String test = bean.test();
        return test;
    }

    @RequestMapping("/setOffset")
    @ResponseBody
    public String setOffset(Integer offset) throws InterruptedException {
        resetOffsetSwitch.set(true);
        boolean tryLock = consumerLock.tryLock(10L, TimeUnit.SECONDS);
        if (tryLock) {
            try {
                System.out.println("获取到锁");
                while (true) {
                    if (consumerWaitingState.get()) {
                        Set<TopicPartition> assignment = consumer.assignment();
                        Map<TopicPartition, Long> map = new HashMap<>();
                        if (assignment.isEmpty()) {
                            System.out.println("重置offset,没有订阅分区");
                        }
                        Long timestamp = System.currentTimeMillis();
                        for (TopicPartition topicPartition : assignment) {
                            map.put(topicPartition, timestamp);
                        }
                        Map<TopicPartition, OffsetAndTimestamp> resMap = consumer.offsetsForTimes(map);
                        TopicPartition topicPartition = new TopicPartition("testoffset", 0);
                        consumer.seek(topicPartition, offset);
                        consumerWaitingState.set(false);
                        resetOffsetSwitch.set(false);
                        condition.signalAll();
                        break;
                    }
                }
            } finally {
                consumerLock.unlock();
            }
        } else {
            System.out.println("没有获取到锁");

        }

        return "ok";
    }

    @RequestMapping("/getOffset")
    @ResponseBody
    public String getOffset(Long time) throws InterruptedException {
        TopicPartition topicPartition = new TopicPartition("testoffset", 0);
        //查询某时间点的offset
        Map<TopicPartition, Long> map = new HashMap<>();
        //map.put(topicPartition, time);
        map.put(topicPartition, 1594214634988L);
        Map<TopicPartition, OffsetAndTimestamp> parMap = consumer.offsetsForTimes(map);
        System.out.println(parMap);
        return "ok";
    }

    @RequestMapping("/setOffset2")
    @ResponseBody
    public String setOffset2(Integer offset) throws InterruptedException {
        consumer.unsubscribe();
        running.getAndSet(false);
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                System.out.println("结束了！");
                break;
            }
            Thread.sleep(200);
        }
        //初始化资源
        running.getAndSet(true);
        consumer = new KafkaConsumer<String, String>(props);
        executorService = Executors.newSingleThreadExecutor();
        consumer.subscribe(topics, new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                System.out.println("====rebalance前");
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                System.out.println("====rebalance后");
                for (TopicPartition topicPartition : collection) {
                    //指定offset拉取
                    consumer.seek(new TopicPartition("testoffset", 0), offset);
                }
            }
        });
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                while (running.get()) {
                    //没有分到分区的消费者record size=0
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(3)); //拉取数据
                    if (records != null && !records.isEmpty()) {
                        for (ConsumerRecord<String, String> record : records) {
                            System.out.printf("====:partition = %s,offset = %d, key = %s, value= %s%n", record.partition(), record.offset(), record.key(), record.value());
                        }
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (!running.get()) {
                        break;
                    }
                }
            }
        });
        return "ok";
    }
}
