package leele.kafkadistributedchatserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import leele.kafkadistributedchatserver.chat.dto.Chat;
import leele.kafkadistributedchatserver.kafka.consumer.Consumer;
import leele.kafkadistributedchatserver.member.entity.Member;
import leele.kafkadistributedchatserver.member.repository.MemberRepository;
import leele.kafkadistributedchatserver.room.entity.Room;
import leele.kafkadistributedchatserver.room.repository.RoomRepository;
import leele.kafkadistributedchatserver.service.ChatService;
import leele.kafkadistributedchatserver.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@ResponseBody
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final ChatService chatService;
    private final RoomService roomService;
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @PostMapping("/roomList")
    public ResponseEntity<Map<String, String>> roomList(@RequestBody final Member params) throws JsonProcessingException {
        try {
            Member entity = memberRepository.findByMemberId(params.getMemberId());
            Map<String, String> rooms = chatService.findAllRoom(entity);

            System.out.println("✅[List of existing rooms] " + rooms);

            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            System.out.println("❗️An error occurred while loading the chat room list. " + e);
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/createRoom")
    public ResponseEntity<String> createRoom(Model model, @RequestParam("roomName") String roomName, @RequestParam("memberId") String memberId) {
        String roomId = null;
        try {
            roomId = chatService.createRoom(roomName, memberId);
        } catch (Exception e) {
            System.out.println("❗️An error occurred while creating the chat room. " + e);
        }

        return ResponseEntity.ok(roomId);
    }

    @PostMapping("/enter")
    public ResponseEntity<String> enterRoom(Chat chat, @RequestBody final Chat params) {
        try {
            Room findRoom = roomRepository.findByRoomId(params.getRoomId());
            Room room = chatService.buildRoom(params.getRoomId(), findRoom.getRoomName());
            chatService.insert(room);
            chatService.setManyToManyRelationships(params.getRoomId(), params.getMemberId());

            System.out.println("✅[" + params.getMemberName() + "]님이" + "[" + params.getRoomId() + "] 채팅방에 입장하였습니다.");

            chat.setMessage(params.getMemberName() + "님이 채팅방에 입장하였습니다.");
            simpMessagingTemplate.convertAndSend("/sub/chat/" + params.getRoomId(), chat);

            return ResponseEntity.ok("Successfully entered the room.");
        } catch (Exception e) {
            System.out.println("❗️An error occurred while entering the chat room. " + e);
            return ResponseEntity.ok("error");
        }
    }

    @PostMapping("/memberList")
    public ResponseEntity<Map<String, String>> memberList(@RequestBody final Room params) throws JsonProcessingException {
        try {
            Room entity = roomRepository.findByRoomId(params.getRoomId());
            Map<String, String> members = roomService.findAllMember(entity);

            System.out.println("✅[List of members in this chat room] " + members);

            return ResponseEntity.ok(members);
        } catch (Exception e) {
            System.out.println("❗️An error occurred while retrieving the list of members in the chat room. " + e);
        }
        return ResponseEntity.ok(null);
    }
}