package com.example.demo.data.kafka;

import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;

public class MyKafkaConsumer2 {

    /**
     * 参考方法:
     *
     * consumer.partitionsFor(topic)
     * 		查询topic的分区信息,当本地没有这个topic的元数据信息的时候会往服务端发送的远程请求
     * 		注意: 没有权限的topic的时候会抛出异常(org.apache.kafka.common.errors.TopicAuthorizationException)
     *     * consumer.assignment()
     *      *      查询指定方式或订阅方式分配到当前消费者的分区信息，还未订阅或重新分配时为空
     * consumer.position(new TopicPartition(topic, 0))
     * 		获取下次拉取的数据的offset, 如果没有offset记录则会抛出异常
     *
     * consumer.committed(new TopicPartition(topic, 0))
     * 		获取已提交的位点信息，如果没有查询到则返回null
     *
     * consumer.beginningOffsets(Arrays.asList(new TopicPartition(topic, 0)));
     * consumer.endOffsets(Arrays.asList(new TopicPartition(topic, 0)));
     * consumer.offsetsForTimes(timestampsToSearch);
     * 		查询最小,最大,或者任意时间的位点信息
     *
     * consumer.seek(new TopicPartition(topic, 0), 10972);
     * consumer.seekToBeginning(Arrays.asList(new TopicPartition(topic, 0)));
     * consumer.seekToEnd(Arrays.asList(new TopicPartition(topic, 0)));
     * 		设置offset给消费者拉取消息
     *
     * consumer.assign(Arrays.asList(new TopicPartition(topic, 0)));
     * 		手动分配消费的topic和分区进行消费，这里不会出发group management操作,指定分区消费数据
     * 		和subscribe方法互斥,如果assign 之后或者之后调用subscribe 则会报错，不允许再进行分配，2方法不能一起使用
     *
     * consumer.subscribe(topics);
     * 		自动发布消费的topic进行消费,这里触发group management操作
     * 		和assign方法互斥,如果subscribe 之后或者之后调用assign 则会报错，不允许再进行分配，2方法不能一起使用
     *
     * 注: group management
     * 		根据group 进行topic分区内部的消费rebanlance
     * 		例如消费的topic包含3个分区，启动了4个相同鉴权的客户端消费
     * 				分区0 -- consumer1   分区1 -- consumer2   分区2 --- consumer3    consumer4则会空跑不消费数据
     *         当分区consumer1挂掉的时候则会出现rebalance，之后变为
     *          	分区0 -- consumer2   分区1 -- consumer3   分区2 --- consumer4
     *
     */
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");//
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "miner"); //消费者组
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000"); //消费者组
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); //开启自动提交
        //props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000"); //自动提交最短时间
        //key反序列化类
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //value反序列化类
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        Collection<String> topics = Arrays.asList("testoffset");
        /*consumer.subscribe(topics, new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {

            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                for (TopicPartition topicPartition:collection) {
                    //指定offset拉取
                    consumer.seek(topicPartition, 0);
                }
            }
        });*/
        consumer.subscribe(topics);
//        原文链接：https://blog.csdn.net/soaring0121/article/details/81330266
/*        OffsetAndMetadata testoffset = consumer.committed(new TopicPartition("testoffset", 0));
        System.out.println("===1:" + testoffset.metadata());
        System.out.println("===2:" + testoffset.offset());*/

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(3)); //拉取数据
        if(records!=null){
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("====:partition = %s,offset = %d, key = %s, value= %s%n", record.partition(), record.offset(), record.key(), record.value());
            }
        }
        /*consumer.commitAsync(new OffsetCommitCallback() {
            @Override
            public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {

            }
        }); */
        //同步提交offset,会阻塞当前线程的运行
        Map<TopicPartition, OffsetAndMetadata> map = new HashMap<>();
        TopicPartition topicPartition = new TopicPartition("testoffset",0);
        OffsetAndMetadata offsetAndMetadata = new OffsetAndMetadata(50,"1");
        map.put(topicPartition,offsetAndMetadata);
        consumer.commitAsync(map, new OffsetCommitCallback() {
            @Override
            public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                System.out.println("-");
            }
        });
        consumer.close();
    }
}
