package leele.kafkadistributedchatserver.service;

import leele.kafkadistributedchatserver.member.entity.Member;
import leele.kafkadistributedchatserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class SignupService {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private MemberRepository memberRepository;

    public Member register(Member params) {
        if (memberRepository.findByMemberId(params.getMemberId()) == null) {
            String encryptedPassword = passwordEncoder.encode(params.getPassword());

            Member member = Member.builder()
                    .memberId(params.getMemberId())
                    .password(encryptedPassword)
                    .memberName(params.getMemberName())
                    .role("ROLE_MEMBER")
                    .joinDate(LocalDateTime.now())
                    .build();

            return memberRepository.save(member);
        } else {
            System.out.println("❗️This ID already exists");
            return null;
        }
    }
}
