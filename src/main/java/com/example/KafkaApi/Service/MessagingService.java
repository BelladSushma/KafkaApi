package com.example.KafkaApi.Service;

import com.example.KafkaApi.Domain.DescribeGroupResult;
import com.example.KafkaApi.Domain.DescribeTopic;
import com.example.KafkaApi.Domain.TopicSpec;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface MessagingService {
    public boolean publishMessage(String topicName, String key, String value);
    public ArrayList<String> consumeMessage(String topicName, String groupID);
    public boolean deleteMessage(String topicName, Integer partitionValue, Integer offsetValue);
    public boolean deleteTopic(TopicSpec topicSpec);
    public Set<String> getTopics();
    public boolean createTopic(TopicSpec topicSpec);
    public ArrayList<DescribeTopic> describeTopic(String topicName) throws ExecutionException, InterruptedException;
    public ArrayList<DescribeGroupResult>  describeGroup(String brokerUrl, String groupID) throws ExecutionException, InterruptedException;

}
