package com.example.demo.controller.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 对外提供http接口，可以从指定offset开始消费，直接提交offset即可，方法2为测试先取消订阅再重新订阅的功能
 */
@Controller
@RequestMapping("/kafka")
public class KafkaController {

    private static KafkaConsumer<String, String> consumer = null;
    private static AtomicBoolean running = new AtomicBoolean(true);
    private static Properties props = new Properties();
    private static Collection<String> topics = Collections.singletonList("testoffset");
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    static {

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
    }

    @RequestMapping("/setOffset")
    @ResponseBody
    public String setOffset(Integer offset) throws InterruptedException {
        TopicPartition topicPartition = new TopicPartition("testoffset", 0);
        consumer.seek(topicPartition, offset);
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
