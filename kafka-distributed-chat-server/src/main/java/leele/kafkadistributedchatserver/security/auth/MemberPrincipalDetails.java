package leele.kafkadistributedchatserver.security.auth;

import leele.kafkadistributedchatserver.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

// Spring Security 에 있는 UserDetails 를 구현한 클래스,
// 이 클래스를 통해 Spring Security 에서 사용자의 정보를 담아둠
public class MemberPrincipalDetails implements UserDetails {
    private final Member member;

    public MemberPrincipalDetails(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    // member 계정의 권한을 담아두기위해
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(member.getRole()));
        return authorities;
    }

    // member 계정의 비밀번호를 담아두기 위해
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    // member 계정의 아이디를 담아두기 위해
    @Override
    public String getUsername() {
        return member.getMemberId();
    }

    // 계정이 만료되지 않았는지를 담아두기 위해 (true: 만료안됨)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겨있지 않았는지를 담아두기 위해 (true: 잠기지 않음)
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정의 비밀번호가 만료되지 않았는지를 담아두기 위해 (true: 만료안됨)
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정이 활성화되어있는지를 담아두기 위해 (true: 활성화됨)
    @Override
    public boolean isEnabled() {
        return true;
    }
}
