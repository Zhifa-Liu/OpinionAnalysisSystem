# coding: "utf-8"
# pip install kafka
# pip install kafka-python  # important
from kafka import KafkaProducer, KafkaClient
from kafka import KafkaConsumer
from kafka.protocol import admin

from config.config import kafka_config_1 as kafka_config


def get_kafka_producer(server=None):
    if server:
        return KafkaProducer(bootstrap_servers=server)
    return KafkaProducer(bootstrap_servers=kafka_config["server"])


def get_kafka_consumer(topic):
    return KafkaConsumer(topic, bootstrap_servers=kafka_config["server"])


def create_topic(topic, num_partitions, timeout_ms, brokers):
    client = KafkaClient(bootstrap_servers=brokers)

    if topic not in client.cluster.topics(exclude_internal_topics=True):  # Topic不存在

        request = admin.CreateTopicsRequest_v0(
            create_topic_requests=[(
                topic,
                num_partitions,
                -1,  # replication unset.
                [],  # Partition assignment.
            )],
            timeout=timeout_ms
        )

        future = client.send(2, request)  # 2是Controller,发送给其他Node都创建失败。
        client.poll(timeout_ms=timeout_ms, future=future)  # 这里

        result = future.value
        # error_code = result.topic_error_codes[0][1]
        client.close()
    else:  # Topic已经存在
        # print("Topic already exists!")
        return


if __name__ == '__main__':
    # producer = get_kafka_producer()
    # producer.send("test", value="abcdef".encode("utf-8"), partition=0)
    # producer.close()
    consumer = get_kafka_consumer("weibo")
    for msg in consumer:
        recv = "%s:%d:%d: key=%s value=%s" % (msg.topic, msg.partition, msg.offset, msg.key, msg.value)
        print(recv)
