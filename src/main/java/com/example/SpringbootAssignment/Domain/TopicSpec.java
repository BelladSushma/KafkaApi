package com.example.SpringbootAssignment.Domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class TopicSpec {
    private String brokerUrl;
    private String topicName;
    private Short replicasCount;
    private Integer partitionCount;
    private Boolean compactedTopic;
    private Map<String,String> topicConfig= new HashMap<String,String>();
}

