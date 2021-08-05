package com.example.SpringbootAssignment.Service;

import com.example.SpringbootAssignment.Domain.TopicSpec;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DeleteTopicsResult;
import org.apache.kafka.clients.admin.RecordsToDelete;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TopicService implements TopicServiceInt{
    @Autowired
    private ConsumerFactory<String,String> consumerFactory;
    private static final Logger logger = LoggerFactory.getLogger(TopicService.class);

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

    @Override
    public void deleteTopic(TopicSpec topicSpec){
        Properties brokerConfig = new Properties();
        brokerConfig.put("bootstrap.servers", topicSpec.getBrokerUrl());

        AdminClient adminClient = AdminClient.create(brokerConfig);
        DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singleton(topicSpec.getTopicName()));
    }

    @Override
    public void deleteMessages(String topicName, Integer partitionValue, Integer offsetValue){
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        AdminClient adminClient = AdminClient.create(props);

        RecordsToDelete recordsToDelete = RecordsToDelete.beforeOffset(offsetValue);
        TopicPartition topicPartition = new TopicPartition(topicName, partitionValue);
        Map<TopicPartition, RecordsToDelete> delete = new HashMap<>();
        delete.put(topicPartition, recordsToDelete);
        adminClient.deleteRecords(delete);
    }
}
