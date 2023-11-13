package leele.kafkadistributedchatserver.kafka.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import leele.kafkadistributedchatserver.chat.dto.Chat;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

public class ChatDeserializer implements Deserializer<Chat> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // 필요에 따라 설정을 초기화하거나 설정값을 받음
    }

    @Override
    public Chat deserialize(String topic, byte[] data) {
        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        // JSON을 LocalDateTime으로 역직렬화
        try {
            String json = "\"2023-11-13T17:04:52.187167\"";
            LocalDateTime localDateTime = objectMapper.readValue(json, LocalDateTime.class);
            System.out.println(localDateTime);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Chat chat = null;
        try {
            if(data == null) {
                System.out.println("❗️Null received at deserializing");
                return null;
            } else {
                chat = objectMapper.readValue(data, Chat.class);
            }
        } catch (IOException e) {
            System.out.println("❗️Error when deserializing byte[] to Chat DTO");
            e.printStackTrace();
        }
        return chat;
    }

    @Override
    public void close() {
        // 리소스를 닫음
    }
}
