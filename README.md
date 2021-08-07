# 
KafkaApi

### Deleting the messages:

Before deletion:

```./kafka-console-consumer.sh --topic test_sushma --bootstrap-server localhost:9092 --from-beginning``` <br />
message1 <br />
message2 <br />
message3 <br />
message4 <br />
message5

After deletion:

```./kafka-console-consumer.sh --topic test_sushma --bootstrap-server localhost:9092 --from-beginning``` <br />
message4 <br />
message5

### Deleting the topic:

Before deleting the topic, topic list in the cluster:

[ <br />
    "demo_topic", <br />
    "test_sushma", <br />
    "__consumer_offsets" <br />
]

After deleting topic (demo_topic), topic list in the cluster:

[ <br />
    "test_sushma", <br />
    "__consumer_offsets" <br />
]
