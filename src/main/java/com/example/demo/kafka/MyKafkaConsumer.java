package com.example.demo.kafka;

import org.apache.kafka.clients.Metadata;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyKafkaConsumer {

    /**
     * 参考方法:
     * <p>
     * consumer.partitionsFor(topic)
     * 查询topic的分区信息,当本地没有这个topic的元数据信息的时候会往服务端发送的远程请求
     * 注意: 没有权限的topic的时候会抛出异常(org.apache.kafka.common.errors.TopicAuthorizationException)
     * <p>
     * consumer.position(new TopicPartition(topic, 0))
     * 获取下次拉取的数据的offset, 如果没有offset记录则会抛出异常
     * <p>
     * consumer.committed(new TopicPartition(topic, 0))
     * 获取已提交的位点信息，如果没有查询到则返回null
     * <p>
     * consumer.beginningOffsets(Arrays.asList(new TopicPartition(topic, 0)));
     * consumer.endOffsets(Arrays.asList(new TopicPartition(topic, 0)));
     * consumer.offsetsForTimes(timestampsToSearch);
     * 查询最小,最大,或者任意时间的位点信息
     * <p>
     * consumer.seek(new TopicPartition(topic, 0), 10972);
     * consumer.seekToBeginning(Arrays.asList(new TopicPartition(topic, 0)));
     * consumer.seekToEnd(Arrays.asList(new TopicPartition(topic, 0)));
     * 指定offset消费,seek调用需要写到ConsumerRebalanceListener中否则会报错No current assignment for partition
     * 或者可以使用assign手动分配分区就可以直接使用seek方法，但是assign不会进行rebalance
     * <p>
     * consumer.assign(Arrays.asList(new TopicPartition(topic, 0)));
     * 手动分配消费的topic和分区进行消费，这里不会出发group management操作,指定分区消费数据
     * 和subscribe方法互斥,如果assign 之后或者之后调用subscribe 则会报错，不允许再进行分配，2方法不能一起使用
     * <p>
     * consumer.subscribe(topics);
     * 自动发布消费的topic进行消费,这里触发group management操作
     * 和assign方法互斥,如果subscribe 之后或者之后调用assign 则会报错，不允许再进行分配，2方法不能一起使用
     * <p>
     * 注: group management
     * 根据group 进行topic分区内部的消费rebanlance
     * 例如消费的topic包含3个分区，启动了4个相同鉴权的客户端消费
     * 分区0 -- consumer1   分区1 -- consumer2   分区2 --- consumer3    consumer4则会空跑不消费数据
     * 当分区consumer1挂掉的时候则会出现rebalance，之后变为
     * 分区0 -- consumer2   分区1 -- consumer3   分区2 --- consumer4
     */
    private static AtomicBoolean running = new AtomicBoolean(true);
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");//
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "miner"); //消费者组
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "10000");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"); //自动提交
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "100"); //每次最多拉取1条

        //（1）实际应用中，消费到的数据处理时长不宜超过max.poll.interval.ms，否则会触发rebalance
        //（2）如果处理消费到的数据耗时，可以尝试通过减小max.poll.records的方式减小单次拉取的记录数（默认是500条）
        //指定consumer两次poll的最大时间间隔（默认5分钟），如果超过了该间隔consumer client会主动向coordinator发起LeaveGroup请求，触发rebalance；然后consumer重新发送JoinGroup请求
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, "10000"); //两次pool最大时间间隔
        //props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000"); //自动提交最短时间
        //key反序列化类
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        //value反序列化类
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        Collection<String> topics = Arrays.asList("testoffset");
        consumer.subscribe(topics, new ConsumerRebalanceListener() {
            @Override
            public void onPartitionsRevoked(Collection<TopicPartition> collection) {
                System.out.println("====rebalance前");
            }

            @Override
            public void onPartitionsAssigned(Collection<TopicPartition> collection) {
                System.out.println("====rebalance后");
                for (TopicPartition topicPartition:collection) {
                    //指定offset拉取
                    consumer.seekToBeginning(Collections.singletonList(new TopicPartition("testoffset", 0)));
                }
            }
        });
        //手动指定分区方式订阅
        /*List<TopicPartition> list = Collections.singletonList(new TopicPartition("testoffset", 0));
        consumer.assign(list);
        List<PartitionInfo> partitionInfos = consumer.partitionsFor("testoffset");
        for (PartitionInfo partitionInfo:partitionInfos) {
            int partitionNumber = partitionInfo.partition();
            OffsetAndMetadata offsetAndMetadata = consumer.committed(new TopicPartition("testoffset", partitionNumber));
            long offset = offsetAndMetadata.offset();
            TopicPartition topicPartition = new TopicPartition("testoffset", partitionNumber);
            consumer.seek(topicPartition, 0);
        }*/
        //查看已提交的offset
/*        OffsetAndMetadata testoffset = consumer.committed(new TopicPartition("testoffset", 0));
        System.out.println("===1:" + testoffset.metadata());
        System.out.println("===2:" + testoffset.offset());*/

        while (true) {
            //没有分到分区的消费者record size=0
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(3)); //拉取数据
            if (records != null && !records.isEmpty()) {
                for (ConsumerRecord<String, String> record : records) {
                    System.out.printf("====:partition = %s,offset = %d, key = %s, value= %s%n", record.partition(), record.offset(), record.key(), record.value());
                }
            }
            Thread.sleep(1000);
            if(!running.get()){
                break;
            }
        }
        //异步提交
        consumer.commitAsync(new OffsetCommitCallback() {
            @Override
            public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {

            }
        });

        consumer.close();
    }
}
