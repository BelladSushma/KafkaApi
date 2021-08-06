package com.example.SpringbootAssignment.Service;

import com.example.SpringbootAssignment.Domain.TopicSpec;

import java.util.Set;
import java.util.concurrent.ExecutionException;

public interface MessagingService {
    public void publishMessage();
    public void consumeMessage(String topicName, String groupID);
    public void deleteMessage(String topicName, Integer partitionValue, Integer offsetValue);
    public void deleteTopic(TopicSpec topicSpec);
    public Set<String> getTopics();
    public boolean createTopic(TopicSpec topicSpec);
    public void describeTopic(String topicName);
    public void describeGroup(String brokerUrl, String groupID) throws ExecutionException, InterruptedException;

}
