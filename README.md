# SpringbootAssignment

## Deleting the messages:

Before deletion:

./kafka-console-consumer.sh --topic test_sushma --bootstrap-server localhost:9092 --from-beginning
message1
message2
message3
message4
message5

After deletion:

./kafka-console-consumer.sh --topic test_sushma --bootstrap-server localhost:9092 --from-beginning
message4
message5

Deleting the topic:

Before deleting the topic, topic list:

[
    "demo_topic",
    "test_sushma",
    "__consumer_offsets"
]

After deleting topic, topiclist:

[
    "test_sushma",
    "__consumer_offsets"
]
