package leele.kafkadistributedchatserver.member.entity;

import jakarta.persistence.*;
import leele.kafkadistributedchatserver.room.entity.Room;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members")
public class Member {
    @Id
    @Column(name = "member_id", unique = true)
    private String memberId;

    @Column(name = "member_password")
    private String password;

    @Column(name = "member_name")
    private String memberName;

    @Column(name = "member_role")
    private String role;

    @Column(name = "join_date")
    private LocalDateTime joinDate;

    // 다대다(N:M) 관계 매핑
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(         // 중간 테이블의 이름과 외래 키 컬럼을 지정
            name = "member_room",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "room_id")
    )
    private Set<Room> rooms = new HashSet<>();
}
