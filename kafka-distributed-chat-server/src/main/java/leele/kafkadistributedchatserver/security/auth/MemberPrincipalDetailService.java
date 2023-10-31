package leele.kafkadistributedchatserver.security.auth;

import leele.kafkadistributedchatserver.member.entity.Member;
import leele.kafkadistributedchatserver.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

// UserDetailsService 를 구현한 클래스
// 스프링 시큐리티가 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데
// password 부분 처리는 알아서 함
@Service
public class MemberPrincipalDetailService implements UserDetailsService {
    @Autowired
    private MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByMemberId(username);

        if(member == null)
            throw new UsernameNotFoundException(username + "을 찾을 수 없습니다.");

        return new MemberPrincipalDetails(member);
    }
}
