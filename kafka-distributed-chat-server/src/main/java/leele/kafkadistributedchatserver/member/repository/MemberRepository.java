package leele.kafkadistributedchatserver.member.repository;

import leele.kafkadistributedchatserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByMemberId(String username);
}
