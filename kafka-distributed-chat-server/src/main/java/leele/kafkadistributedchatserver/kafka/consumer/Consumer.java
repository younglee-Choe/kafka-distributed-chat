package leele.kafkadistributedchatserver.kafka.consumer;

import leele.kafkadistributedchatserver.chat.dto.Chat;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer {
    public static void consume(String topic) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "127.0.0.1:29092,127.0.0.1:39092,127.0.0.1:49092");
        props.put("group.id", "leele-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "leele.kafkadistributedchatserver.kafka.deserializer.ChatDeserializer");
        KafkaConsumer<String, Chat> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList(topic));

        try {
            while (true) {
                ConsumerRecords<String, Chat> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Chat> record : records) {
                    Chat chat = record.value();
                    System.out.println("🫧Received message: " + chat.getMessage());
                    // 여기서 처리 로직을 작성
                }
            }
        } finally {
            consumer.close();
        }
    }
}

