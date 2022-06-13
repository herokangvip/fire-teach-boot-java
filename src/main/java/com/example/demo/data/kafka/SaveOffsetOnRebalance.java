package com.example.demo.data.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;

public class SaveOffsetOnRebalance implements ConsumerRebalanceListener {
    private Consumer<String, String> consumer;

    //初始化方法，传入consumer对象，否则无法调用外部的consumer对象，必须传入
    public SaveOffsetOnRebalance(Consumer<String, String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> collection) {

        //提交偏移量 主要是consumer.commitSync(toCommit); 方法
        System.out.println("*- in ralance:onPartitionsRevoked");
        //commitQueue 是我本地已消费消息的一个队列 是一个linkedblockingqueue对象
        /*while (!commitQueue.isEmpty()) {
            Map<TopicPartition, OffsetAndMetadata> toCommit = commitQueue.poll();
            consumer.commitSync(toCommit);
        }*/
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> collection) {
        //rebalance之后 获取新的分区，获取最新的偏移量，设置拉取分量
        System.out.println("*- in ralance:onPartitionsAssigned  ");
        for (TopicPartition partition : collection) {
            System.out.println("*- partition:"+partition.partition());

            //获取消费偏移量，实现原理是向协调者发送获取请求
            OffsetAndMetadata offset = consumer.committed(partition);
            //设置本地拉取分量，下次拉取消息以这个偏移量为准
            consumer.seek(partition, offset.offset());
        }
    }

}
