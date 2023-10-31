package leele.kafkadistributedchatserver.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import leele.kafkadistributedchatserver.member.entity.Member;
import leele.kafkadistributedchatserver.member.repository.MemberRepository;
import leele.kafkadistributedchatserver.service.ChatRoom;
import leele.kafkadistributedchatserver.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/room")
@RequiredArgsConstructor
public class RoomController {
    private final ChatService chatService;
    private final MemberRepository memberRepository;

    @PostMapping("/roomList")
    public ResponseEntity<Map<String, String>> roomList(@RequestBody final Member params) throws JsonProcessingException {
        try {
            Member entity = memberRepository.findByMemberId(params.getMemberId());
            Map<String, String> rooms = chatService.findAllRoom(entity);

            System.out.println("✅List of existing rooms: " + rooms);

            return ResponseEntity.ok(rooms);
        } catch (Exception e) {
            System.out.println("❗️An exception occurred while loading the chat room list: " + e);
        }
        return ResponseEntity.ok(null);
    }

    @PostMapping("/createRoom")
    public ResponseEntity<String> createRoom(Model model, @RequestParam("roomName") String roomName, @RequestParam("memberId") String memberId) {
        try {
            chatService.createRoom(roomName, memberId);
        } catch (Exception e) {
            System.out.println("❗️An exception occurred while creating the chat room: " + e);
        }

        return ResponseEntity.ok("Successfully created room");
    }
}