package leele.kafkadistributedchatserver.service;

import jakarta.annotation.PostConstruct;
import leele.kafkadistributedchatserver.member.entity.Member;
import leele.kafkadistributedchatserver.member.repository.MemberRepository;
import leele.kafkadistributedchatserver.room.entity.Room;
import leele.kafkadistributedchatserver.room.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private Map<String, ChatRoom> chatRooms;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private MemberRepository memberRepository;


    @PostConstruct
    private void init() {
        chatRooms = new LinkedHashMap<>();
    }

    public Room insert(Room room) {
        return roomRepository.save(room);
    }

    // Member와 Room의 다대다(N:M) 관계 변경
    public void setManyToManyRelationships(String roomId, String memberId) {
        Room room = roomRepository.findByRoomId(roomId);
        Member member = memberRepository.findByMemberId(memberId);

        if (room.getMembers() == null) {
            room.setMembers(new HashSet<>());
        }

        member.getRooms().add(room);
        room.getMembers().add(member);

        System.out.println("⚙️Add room to member: " + member);
        System.out.println("️⚙️Add member to room: " + room);

        // 관계 변경 반영
        memberRepository.save(member);
        roomRepository.save(room);
    }

    // member가 가진 채팅방 정보
    public Map<String, String> findAllRoom(Member member) {
        Set<Room> rooms = member.getRooms();
        Map<String, String> roomsMap = new HashMap<>();

        for (Room room : rooms) {
            String roomId = room.getRoomId();
            String roomName = room.getRoomName();

            roomsMap.put(roomId, roomName);
        }

        return roomsMap;
    }

    public ChatRoom findRoomById(String roomId) {
        System.out.println("✅Find Room By ID: " + roomId);
        return chatRooms.get(roomId);
    }

    // 랜덤 UUID로 구별 ID를 가진 채팅방 객체를 생성하고 Database에 저장
    public String createRoom(String roomName, String memberId) {
        String randomId = UUID.randomUUID().toString();
        Room room = buildRoom(randomId, roomName);
        insert(room);
        setManyToManyRelationships(randomId, memberId);
        System.out.println("✅Created a Room! " + room);

        return randomId;
    }

    public Room buildRoom(String roomId, String roomName) {

        return Room.builder()
                .roomId(roomId)
                .roomName(roomName)
                .build();
    }
}