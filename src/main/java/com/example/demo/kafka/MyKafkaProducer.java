package com.example.demo.kafka;

import org.apache.kafka.clients.producer.*;

import java.sql.Timestamp;
import java.util.Properties;

public class MyKafkaProducer {

    public static void main(String[] args) throws Exception, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        props.put(ProducerConfig.ACKS_CONFIG, "all");// leader 确认机制 0 1 all
        props.put(ProducerConfig.RETRIES_CONFIG, 1);// 重试次数
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);// 生产者批发送大小
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);// 生产者达不到批发送大小，最短等待时间
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);// RecordAccumulator 缓冲区大小
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");// key的序列化器
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");// value的序列化器

        Producer<String, String> producer = new KafkaProducer<>(props);

        try {
            for (int i = 0; i < 100; i++) {
                //指定了key会按keyHash对分区数取余决定发到哪个分区，没有指定key会自动生成一个自增id
                ProducerRecord<String, String> record = new ProducerRecord<>("testoffset", Integer.toString(i), Integer.toString(i));
                //默认都是异步发送，如果想同步发送producer.send(xxx).get()单条发送
                producer.send(record, new Callback() {
                    @Override
                    public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                        if (exception != null) {
                            //当存在异常的时候,失败逻辑
                            System.out.println("发送异常");
                        } else {
                            //发送成功之后这里的RecordMetadata内包含此条消息写入到JDQ集群的分区和对应的位点信息等
                            System.out.println("success");
                        }
                    }
                });
            }
            /**
             *  flush :  执行flush代表内存中的消息数据都会send到服务端，可根据callback来判断成功与否
             *  自己控制flush
             *  注意：这里可发送一批数据之后再掉flush，达到批量的效果
             */
            producer.flush();
        } finally {
            producer.close(); //关闭消费者
        }
    }
}
