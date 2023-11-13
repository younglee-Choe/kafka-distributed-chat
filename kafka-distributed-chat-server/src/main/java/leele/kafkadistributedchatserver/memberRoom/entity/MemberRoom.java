package leele.kafkadistributedchatserver.memberRoom.entity;

import jakarta.persistence.*;
import leele.kafkadistributedchatserver.member.entity.Member;
import leele.kafkadistributedchatserver.room.entity.Room;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "member_room")
public class MemberRoom {
    @EmbeddedId
    private MemberRoomId id;

    @ManyToOne
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "join_date")
    private LocalDateTime joinDate;
}
