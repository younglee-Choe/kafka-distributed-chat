package leele.kafkadistributedchatserver.chat.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Getter
@Setter
@Builder
public class Chat {
    private String roomId;
    private String roomName;
    private String memberId;
    private String memberName;
    private String message;
    private LocalDateTime date;

    public Chat() {
        // 기본 생성자 추가
    }

    public Chat(String roomId, String roomName, String memberId, String memberName, String message, LocalDateTime date) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.memberId = memberId;
        this.memberName = memberName;
        this.message = message;
        this.date = date;
    }
}