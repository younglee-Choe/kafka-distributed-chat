package leele.kafkadistributedchatserver.controller;

import leele.kafkadistributedchatserver.chat.dto.Chat;
import leele.kafkadistributedchatserver.kafka.consumer.Consumer;
import leele.kafkadistributedchatserver.kafka.producer.Producer;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ChatController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;    // 특정 broker로 메시지 전달

    // 클라이언트가 send할 수 있는 경로
    // WebSocketConfig 설정한 ApplicationDestinationPrefixes와 @MessageMapping 경로 병합
    // "/pub/chat/enter"
    @MessageMapping("/chat/enter")
    public void enter(Chat chat, SimpMessageHeaderAccessor accessor) {
        System.out.println(chat.getMemberName() + "님이 채팅방에 입장하였습니다.");
        chat.setMessage(chat.getMemberName() + "님이 채팅방에 입장하였습니다.");

        // simpMessagingTemplate.convertAndSend를 통해 /sub/chat/{roomId} 채널을 구독 중인 클라이언트에게 메시지를 전송
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chat.getRoomId(), chat);
    }

    @MessageMapping("/chat")
    public void sendMessage(Chat chat){
        System.out.println("✉️[Chat]: " + chat);
        simpMessagingTemplate.convertAndSend("/sub/chat/" + chat.getRoomId(), chat);

        // Kafka에 지속적으로 메시지 저장
        Producer.produce(chat);
    }

    @GetMapping("/chat/history/{memberId}")
    public ResponseEntity<String> getChatHistory(@RequestParam("roomId") String roomId, @PathVariable String memberId) {
        // 사용자가 채팅방에 재입장하면 Kafka에서 roomId(Topic name)로 이전 대화 내용 불러오기
        try {
            Consumer.consume(roomId, memberId);
        } catch (Exception e) {
            System.out.println("❗️An error occurred while reading a message from Kafka. " + e);
            return ResponseEntity.ok("An error occurred while reading a message from Kafka.");
        }

        return ResponseEntity.ok("Successfully completed reading messages from Kafka.");
    }
}