package com.example.SpringbootAssignment.Service;

import com.example.SpringbootAssignment.Domain.TopicSpec;

import java.util.Set;

public interface TopicServiceInt {
    public Set<String> getTopics();
    public void deleteTopic(TopicSpec topicSpec);
    public void deleteMessages(String topicName, Integer partitionValue, Integer offsetvalue);
}
