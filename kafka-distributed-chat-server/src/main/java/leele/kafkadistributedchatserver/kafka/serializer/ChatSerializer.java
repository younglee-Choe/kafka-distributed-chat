package leele.kafkadistributedchatserver.kafka.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import leele.kafkadistributedchatserver.chat.dto.Chat;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatSerializer implements Serializer<Chat>, Serializable {
    @Override
    public byte[] serialize(String topic, Chat data) {
        try {
            if (data == null)
                return null;

            ObjectMapper objectMapper = new ObjectMapper();

            objectMapper.registerModule(new JavaTimeModule());

            // LocalDateTime을 JSON으로 직렬화
            try {
                LocalDateTime localDateTime = LocalDateTime.now();
                String json = objectMapper.writeValueAsString(localDateTime);
                System.out.println(json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error when serializing Chat to byte[]");
        }
    }
}