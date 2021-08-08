package com.example.KafkaApi.Controller;

import com.example.KafkaApi.Domain.DescribeGroupResult;
import com.example.KafkaApi.Domain.DescribeTopic;
import com.example.KafkaApi.Domain.TopicSpec;
import com.example.KafkaApi.Service.*;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;

@RestController
@RequestMapping("/")
public class KafkaController {
    @Autowired
    private MessagingService messagingService;
    private static final Logger logger = LoggerFactory.getLogger(KafkaController.class);

    @PostMapping(value = "/publish")
    public ResponseEntity<String> publishMessage(@RequestParam String topicName,
                                                          @RequestParam String key,
                                                          @RequestParam String value) {
        ResponseEntity<String> response = null;

        boolean res = messagingService.publishMessage(topicName,key,value);
        if(res)
                return new ResponseEntity<String>("Message sent", HttpStatus.OK);
        else
                return new ResponseEntity<String>("Topic is not present", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/list")
    public ResponseEntity<Set<String>> listTopicsInCluster(){
        Set<String> result;
        result = messagingService.getTopics();
        return new ResponseEntity<Set<String>>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/topic")
    public ResponseEntity<String> createTopic(@RequestBody TopicSpec topicSpec){
        messagingService.createTopic(topicSpec);
        return ResponseEntity.ok().body("created the topic " + topicSpec.getTopicName() + "\nNo of Partitions= " + topicSpec.getPartitionCount()
                    + "\nNo of Replicas = " + topicSpec.getReplicasCount());
    }

    @GetMapping(value = "/describe/{topic}")
    public ResponseEntity<ArrayList<DescribeTopic>> describeTopic(@PathVariable("topic") String topicName)
            throws ExecutionException, InterruptedException {
        return new ResponseEntity<ArrayList<DescribeTopic>>(messagingService.describeTopic(topicName), HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<String> deleteTopic(@RequestBody TopicSpec topicSpec){
        boolean res = messagingService.deleteTopic(topicSpec);
        if(res)
            return new ResponseEntity<String>("deleted the topic " + topicSpec.getTopicName(),HttpStatus.OK);
        else
            return new ResponseEntity<String>("Topic is not present!!", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(value = "/group")
    public ResponseEntity<ArrayList<DescribeGroupResult>> describeGroup(@RequestParam String brokerUrl, @RequestParam String groupID) throws ExecutionException, InterruptedException {
        return new ResponseEntity<ArrayList<DescribeGroupResult>>(messagingService.describeGroup(brokerUrl, groupID),
        HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/{topic}")
    public ResponseEntity<String> deleteMsgs(@PathVariable("topic") String topicName, @RequestParam Integer partitionValue, @RequestParam Integer offsetValue){
        boolean res = messagingService.deleteMessage(topicName, partitionValue, offsetValue);
        if(res)
            return ResponseEntity.ok().body("Deleted the records");
        else
            return ResponseEntity.ok().body("Topic is not present");
    }

    @GetMapping(value = "/consume")
    public ResponseEntity<ArrayList<String>> consumeMessage(@RequestParam String topicName, @RequestParam String groupID){
        return new ResponseEntity<ArrayList<String>>((messagingService.consumeMessage(topicName, groupID))
        ,HttpStatus.OK);
    }

}

