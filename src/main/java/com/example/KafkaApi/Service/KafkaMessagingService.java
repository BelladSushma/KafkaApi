package com.example.KafkaApi.Service;

import com.example.KafkaApi.Domain.TopicSpec;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class KafkaMessagingService implements MessagingService{

    @Autowired
    private ConsumerFactory<String,String> consumerFactory;
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessagingService.class);

    //listing the topics present in the cluster
    @Override
    public Set<String> getTopics(){
        try (Consumer<String, String> consumer =
                     consumerFactory.createConsumer()) {
            Map<String, List<PartitionInfo>> map = consumer.listTopics();
            logger.info(java.lang.String.format("list of the topics", map));
            for (String i : map.keySet()){
                System.out.println(i);
            }
            return map.keySet();
        }
    }

    //creating the topic
    @Override
    public boolean createTopic(TopicSpec topicSpec) {
        NewTopic topic = new NewTopic(topicSpec.getTopicName(), topicSpec.getPartitionCount(), topicSpec.getReplicasCount());
        if(topicSpec.getCompactedTopic()){
            topicSpec.getTopicConfig().put("cleanup.policy","compact");
        }else{
            topicSpec.getTopicConfig().put("cleanup.policy","delete");
        }
        topic.configs(topicSpec.getTopicConfig());

        Properties brokerConfig = new Properties();
        brokerConfig.put("bootstrap.servers", topicSpec.getBrokerUrl());

        AdminClient adminClient = AdminClient.create(brokerConfig);
        CreateTopicsResult result = adminClient.createTopics(Collections.singleton(topic));
        while(!result.all().isDone()){
            logger.debug("waiting for the topic to be created");
        }
        logger.info("Topic is created");
        return true;
    }

    //displaying topic details
    @Override
    public boolean describeTopic(String topicName) {

        Consumer<String, String> consumer = consumerFactory.createConsumer();
        Map<String, List<PartitionInfo>> map = consumer.listTopics();
        if (map.keySet().contains(topicName)) {

            logger.info("Fetching endOffsets For Topic : {} ");

            logger.info("Fetching endOffsets For Topic : {} ", topicName);

            List<PartitionInfo> partitions = consumer.partitionsFor(topicName);

            for (PartitionInfo partition : partitions) {
                logger.debug("Topic :" + topicName + "\tPartition " + partition.partition());
                TopicPartition topicPartition = new TopicPartition(topicName, partition.partition());
                List<TopicPartition> listPartition = Arrays.asList(topicPartition);
                consumer.assign(listPartition);
                consumer.seekToEnd(listPartition);
                long endOffset = consumer.position(topicPartition);
                System.out.println("Topic :" + topicName + "\tPartition : " + partition.partition() + "\tLogEndOffset : " + endOffset
                );
            }
            consumer.close();
            return true;
        }
        else{
            logger.info("Topic is not present!!");
            return false;
        }
    }

    //deleting the topic
    @Override
    public void deleteTopic(TopicSpec topicSpec){
        Properties brokerConfig = new Properties();
        brokerConfig.put("bootstrap.servers", topicSpec.getBrokerUrl());

        AdminClient adminClient = AdminClient.create(brokerConfig);
        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singleton(topicSpec.getTopicName()));
    }

    //sending message to the topic
    @Override
    public void publishMessage(String topicName, String key, String value) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer<>(props);
        ProducerRecord<String,String> message=new ProducerRecord<>(topicName,key,value);
        producer.send(message);
        producer.close();
    }

    //consuming the messages from the topic
    @Override
    public void consumeMessage(String topicName, String groupID) {
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", groupID);
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.setProperty("enable.auto.commit", "true");
        props.setProperty("auto.commit.interval.ms", "1000");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        String topic = topicName;
        consumer.subscribe(Arrays.asList(topic));
        System.out.println("topic name = " + topic);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
        }

    }

    //deleting the messages from a given topic
    @Override
    public void deleteMessage(String topicName, Integer partitionValue, Integer offsetValue){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        AdminClient adminClient = AdminClient.create(props);

        RecordsToDelete recordsToDelete = RecordsToDelete.beforeOffset(offsetValue);
        TopicPartition topicPartition = new TopicPartition(topicName, partitionValue);
        Map<TopicPartition, RecordsToDelete> delete = new HashMap<>();
        delete.put(topicPartition, recordsToDelete);
        adminClient.deleteRecords(delete);
    }

    //describing the consumer group
    @Override
    public void describeGroup(String brokerUrl, String groupID) throws ExecutionException, InterruptedException {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", brokerUrl);
        properties.put("group.id", groupID);
        AdminClient client = AdminClient.create(properties);

        ListConsumerGroupOffsetsResult listConsumerGroupOffsetsResult = client.listConsumerGroupOffsets(groupID);
        if (!listConsumerGroupOffsetsResult.partitionsToOffsetAndMetadata().isDone()) {
        }
        System.out.println("Consumer group details: \n");
            listConsumerGroupOffsetsResult.partitionsToOffsetAndMetadata().get().forEach((k, v) -> {

                System.out.println("TOPIC: " + k.topic() + "  Partition: " + k.partition() + "  offset: " + v.offset());
            });
    }
}
