# -*- coding: utf-8 -*-
from util import kafka_util
import time


def to_kafka(topic):
    producer = kafka_util.get_kafka_producer()
    count = 0
    with open("weibo_topic.txt", encoding='utf-8') as f:
        while True:
            try:
                # time.sleep(2)
                line = f.readline()
                if line:
                    # producer.send(topic, value=str(line.strip()).encode("utf-8"))
                    count += 1
                else:
                    continue
            except:
                print("continue")
    print(count)


if __name__ == '__main__':
    to_kafka("weibo_ods")

