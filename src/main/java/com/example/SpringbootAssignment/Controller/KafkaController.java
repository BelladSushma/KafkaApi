package com.example.SpringbootAssignment.Controller;

import com.example.SpringbootAssignment.Domain.TopicSpec;
import com.example.SpringbootAssignment.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/")
public final class KafkaController {
    @Autowired
    private MessagingService messagingService;

    @PostMapping(value = "/publish")
    public String sendMessageToKafkaTopic() {
        messagingService.publishMessage();
        return "Message Published";
    }

    @GetMapping(value = "/list")
    public Set<String> list(){
        Set<String> result;
        result = messagingService.getTopics();
        return result;
    }

    @PostMapping(value = "/topic")
    public String createTopic(@RequestBody TopicSpec topicSpec){
        messagingService.createTopic(topicSpec);
        return "created the topic " + topicSpec.getTopicName() + "\nNo of Partitions= " + topicSpec.getPartitionCount()
                + "\nNo of Replicas = " + topicSpec.getReplicasCount();
    }

    @GetMapping(value = "/describe/{topic}")
    public String describeTopic(@PathVariable("topic") String topicName){
        messagingService.describeTopic(topicName);
        return "described the topic";
    }

    @DeleteMapping(value = "/delete")
    public String deleteTopic(@RequestBody TopicSpec topicSpec){
        messagingService.deleteTopic(topicSpec);
        return "deleted the topic " + topicSpec.getTopicName();
    }

    @GetMapping(value = "/group")
    public String describeGroup(@RequestParam String brokerUrl, @RequestParam String groupID) throws ExecutionException, InterruptedException {
        messagingService.describeGroup(brokerUrl, groupID);
        return "described the group";
    }

    @DeleteMapping(value = "/delete/{topic}")
    public String deleteMsgs(@PathVariable("topic") String topicName, @RequestParam Integer partitionValue, @RequestParam Integer offsetValue){
        messagingService.deleteMessage(topicName, partitionValue, offsetValue);
        return "deleted the records";
    }

    @GetMapping(value = "/consume")
    public void consumeMessage(@RequestParam String topicName, @RequestParam String groupID){
        messagingService.consumeMessage(topicName, groupID);
    }

}