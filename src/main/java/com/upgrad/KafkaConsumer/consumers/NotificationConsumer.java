package com.upgrad.KafkaConsumer.consumers;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Set;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


public class NotificationConsumer {

  public static void main(String[] args) {

    //Consumer Properties
    Properties props = new Properties();
    props.setProperty("bootstrap.servers", "ec2-user@ec2-54-90-101-165.compute-1.amazonaws.com:9092");
    props.setProperty("group.id", "test");
    props.setProperty("enable.auto.commit", "true");
    props.setProperty("auto.commit.interval.ms", "1000");
    props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
    KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
    consumer.subscribe(Arrays.asList("notification-topic"));

    //Prints the topic subscription list
    Set<String> subscribedTopics = consumer.subscription();
    subscribedTopics.stream().forEach(System.out::println);

    try {
      while (true) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records)
          System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
      }
    } finally {
      consumer.close();
    }
  }
}
