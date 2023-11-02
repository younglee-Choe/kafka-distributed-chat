package leele.kafkadistributedchatserver.kafka.serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import leele.kafkadistributedchatserver.chat.dto.Chat;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.io.Serializable;

public class ChatSerializer implements Serializer<Chat>, Serializable {
    @Override
    public byte[] serialize(String topic, Chat data) {
        try {
            if (data == null)
                return null;

            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error when serializing Chat to byte[]");
        }
    }
}