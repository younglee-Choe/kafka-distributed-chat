package leele.kafkadistributedchatserver.service;

import leele.kafkadistributedchatserver.member.entity.Member;
import leele.kafkadistributedchatserver.room.entity.Room;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class RoomService {

    public Map<String, String> findAllMember(Room room) {
        Set<Member> members = room.getMembers();
        Map<String, String> membersMap = new HashMap<>();

        for (Member member : members) {
            String memberId = member.getMemberId();
            String memberName = member.getMemberName();

            membersMap.put(memberId, memberName);
        }

        return membersMap;
    }
}
