package leele.kafkadistributedchatserver.kafka.producer;

import leele.kafkadistributedchatserver.chat.dto.Chat;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;

import java.util.Properties;

public class Producer {
    @Value("${kafka.brokers}")
    private static String brokers;

    @Value("${kafka.group.id}")
    private static String groupId;

    public static void produce(Chat chat) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", groupId);
        // Serializer를 사용해 Byte 타입으로 데이터를 직렬화한 후 Kafka에 전송
        props.put("key.serializer" , "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "leele.kafkadistributedchatserver.kafka.serializer.ChatSerializer");

        KafkaProducer<String, Chat> producer = new KafkaProducer<>(props);

        Chat kafkaChat = Chat.builder()
                .roomId(chat.getRoomId())
                .roomName(chat.getRoomName())
                .memberId(chat.getMemberId())
                .memberName(chat.getMemberName())
                .message(chat.getMessage())
                .date(chat.getDate())
                .build();

        try {
            // Producer가 보낼 Record를 생성
            ProducerRecord<String, Chat> record = new ProducerRecord<>(chat.getRoomId(), kafkaChat);
            // Producer는 Record를 전송하고 결과값을 콜백 인스턴스가 처리 (비동기방식)
            producer.send(record, new ProducerCallback(record));
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            producer.close();
        }
    }
}