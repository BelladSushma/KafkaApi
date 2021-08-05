package com.example.SpringbootAssignment.Controller;

import com.example.SpringbootAssignment.Domain.TopicSpec;
import com.example.SpringbootAssignment.Service.ProducerService;
import com.example.SpringbootAssignment.Service.TopicService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/")
public final class KafkaController {
    private final TopicService topicService;
    private final ProducerService producerService;

    public KafkaController(TopicService topicService, ProducerService producerService) {
        this.topicService = topicService;
        this.producerService = producerService;
    }

    @PostMapping(value = "/publish")
    public String sendMessageToKafkaTopic(@RequestParam String message, @RequestParam String topicName) {
        producerService.sendMessage(message, topicName);
        return "Message Published: " + message + "to the topic " + topicName;
    }

    @GetMapping(value = "/list")
    public Set<String> list(){
        Set<String> result;
        result = topicService.getTopics();
        return result;
    }


    @DeleteMapping(value = "/delete")
    public String deleteTopic(@RequestBody TopicSpec topicSpec){
        topicService.deleteTopic(topicSpec);
        return "deleted the topic " + topicSpec.getTopicName();
    }

    @DeleteMapping(value = "/delete/{topic}")
    public String deleteMsgs(@PathVariable("topic") String topicName, @RequestParam Integer partitionValue, @RequestParam Integer offsetValue){
        topicService.deleteMessages(topicName, partitionValue, offsetValue);
        return "deleted the records";
    }

}