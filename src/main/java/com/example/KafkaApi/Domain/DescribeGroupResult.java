package com.example.KafkaApi.Domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DescribeGroupResult {
    private String topicName;
    private Integer partitionValue;
    private Long offsetValue;
}
