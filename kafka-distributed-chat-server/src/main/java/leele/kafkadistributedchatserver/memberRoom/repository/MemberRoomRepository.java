package leele.kafkadistributedchatserver.memberRoom.repository;

import leele.kafkadistributedchatserver.member.entity.Member;
import leele.kafkadistributedchatserver.memberRoom.entity.MemberRoom;
import leele.kafkadistributedchatserver.memberRoom.entity.MemberRoomId;
import leele.kafkadistributedchatserver.room.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@EnableJpaRepositories
@Repository
public interface MemberRoomRepository extends JpaRepository<MemberRoom, MemberRoomId> {
    MemberRoom findByMemberAndRoom(Member member, Room room);
}