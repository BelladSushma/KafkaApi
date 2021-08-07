package com.example.KafkaApi.Service;

import com.example.KafkaApi.Domain.TopicSpec;

import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface MessagingService {
    public void publishMessage(String topicName, String key, String value);
    public void consumeMessage(String topicName, String groupID);
    public void deleteMessage(String topicName, Integer partitionValue, Integer offsetValue);
    public void deleteTopic(TopicSpec topicSpec);
    public Set<String> getTopics();
    public boolean createTopic(TopicSpec topicSpec);
    public boolean describeTopic(String topicName);
    public void describeGroup(String brokerUrl, String groupID) throws ExecutionException, InterruptedException;

}
