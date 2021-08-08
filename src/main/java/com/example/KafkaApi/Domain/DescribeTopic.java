package com.example.KafkaApi.Domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class DescribeTopic {
    private String topicName;
    private Integer partition;
    private List<Integer> replicas;
    private List<Integer> insyncreplicas;
    private Integer leader;
}
