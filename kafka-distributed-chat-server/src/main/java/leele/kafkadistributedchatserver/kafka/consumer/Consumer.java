package leele.kafkadistributedchatserver.kafka.consumer;

import leele.kafkadistributedchatserver.chat.dto.Chat;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.*;

@RestController
public class Consumer {
    @Value("${kafka.brokers}")
    private static String brokers;

    @Value("${kafka.group.id}")
    private static String groupId;

    private static SimpMessagingTemplate messagingTemplate;

    public static void consume(String roomId, String memberId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "false");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "earliest");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "leele.kafkadistributedchatserver.kafka.deserializer.ChatDeserializer");

        List<Chat> chatList = new ArrayList<>();
        Thread mainThread = Thread.currentThread();

        KafkaConsumer<String, Chat> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(roomId));

        // main 스레드 종료 시 별도 thread로 kafkaConsumer wakeup() 메서드를 호출
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                consumer.wakeup();
                try {
                    mainThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            while (true) {
                ConsumerRecords<String, Chat> records = consumer.poll(Duration.ofMillis(100));
                for (ConsumerRecord<String, Chat> record : records) {
                    System.out.printf("🫧[Consumer] partition = %s, offset = %d, key = %s, value = %s %n",
                            record.partition(), record.offset(), record.key(), record.value());

                    chatList.add(record.value());
                }

                // Kafka에 저장된 모든 메시지 읽은 후 종료
                if(!chatList.isEmpty() && records.isEmpty()) {
                    sendDataToRestAPI(chatList, roomId, memberId);
                    break;
                }
            }
        } catch (WakeupException e) {
            System.out.println("❗️Wakeup exception has been called. " + e);
        } finally {
            System.out.println("Finally consumer is closing...");
            consumer.close();
        }
    }

    @Autowired
    public void StompMessageSender(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public static void sendDataToRestAPI(List<Chat> chat, String roomId, String memberId) {
        messagingTemplate.convertAndSend("/sub/chat/init/" + roomId + memberId, chat);
    }
}