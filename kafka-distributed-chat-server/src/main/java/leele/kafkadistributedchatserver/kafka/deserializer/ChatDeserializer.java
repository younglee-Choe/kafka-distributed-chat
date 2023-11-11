package leele.kafkadistributedchatserver.kafka.deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import leele.kafkadistributedchatserver.chat.dto.Chat;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class ChatDeserializer implements Deserializer<Chat> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // 필요에 따라 설정을 초기화하거나 설정값을 받음
    }

    @Override
    public Chat deserialize(String topic, byte[] data) {
        ObjectMapper mapper = new ObjectMapper();
        Chat chat = null;
        try {
            if(data == null) {
                System.out.println("❗️Null received at deserializing");
                return null;
            } else {
                System.out.println("Deserializing...");
                chat = mapper.readValue(data, Chat.class);
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
