package leele.kafkadistributedchatserver.memberRoom.entity;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class MemberRoomId implements Serializable {
    private String memberId;
    private String roomId;

    public MemberRoomId() {
    }
}