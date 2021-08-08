# KafkaApi

This is a kafka admin api which does the admin operations like creating topic, describing topic, deleting topic and it will expose rest api to publish and consume messages from kafka topic.

In order to run the application, install zookeeper and kafka.

## Create Topic

```aidl
curl --location --request POST 'localhost:8080/topic' \
--header 'Content-Type: application/json' \
--data-raw '{
    "brokerUrl" : "localhost:9092",
    "topicName" : "hello_topicc",
    "replicasCount" : 1,
    "partitionCount" : 3,
    "compactedTopic" : false
}'
```

## Publish Messages

```aidl
curl --location --request POST 'localhost:8080/publish?value=Good Evening&topicName=hello_topic1&key=2' \
--header 'Content-Type: application/json' \
--data-raw '{
    "brokerUrl" : "localhost:9092",
    "topicName" : "hello_sushma"
}'
```

## Consume Messages

```aidl
curl --location --request GET 'localhost:8080/consume?topicName=hello_topic1&groupID=testing'
```

## Listing the topics in a cluster

```aidl
curl --location --request GET 'localhost:8080/list'
```

## Describing the topic

```aidl
curl --location --request GET 'localhost:8080/describe/hello_topic1'
```

## Describing the consumer group

```aidl
curl --location --request GET 'localhost:8080/group?brokerUrl=localhost:9092&groupID=testing'
```

## Deleting the topic

```aidl
curl --location --request DELETE 'localhost:8080/delete' \
--header 'Content-Type: application/json' \
--data-raw '{
    "brokerUrl" : "localhost:9092",
    "topicName" : "hello_topicc"
}'
```

## Deleting the Messages

```aidl
curl --location --request DELETE 'localhost:8080/delete/hello_topic1?partitionValue=1&offsetValue=3'
```

