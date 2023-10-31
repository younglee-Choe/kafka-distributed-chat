package leele.kafkadistributedchatserver.room.entity;

import jakarta.persistence.*;
import leele.kafkadistributedchatserver.member.entity.Member;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms")
public class Room {
    @Id
    @Column(name = "room_id", unique = true)
    private String roomId;

    @Column(name = "room_name")
    private String roomName;

    @ManyToMany(mappedBy = "rooms")
    private Set<Member> members = new HashSet<>();
}
