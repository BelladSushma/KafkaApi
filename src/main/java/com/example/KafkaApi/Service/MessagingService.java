package com.example.KafkaApi.Service;

import com.example.KafkaApi.Domain.TopicSpec;

import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface MessagingService {
    public boolean publishMessage(String topicName, String key, String value);
    public void consumeMessage(String topicName, String groupID);
    public boolean deleteMessage(String topicName, Integer partitionValue, Integer offsetValue);
    public boolean deleteTopic(TopicSpec topicSpec);
    public Set<String> getTopics();
    public boolean createTopic(TopicSpec topicSpec);
    public boolean describeTopic(String topicName);
    public void describeGroup(String brokerUrl, String groupID) throws ExecutionException, InterruptedException;

}
