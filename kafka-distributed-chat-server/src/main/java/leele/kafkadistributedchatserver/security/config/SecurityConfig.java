package leele.kafkadistributedchatserver.security.config;

import leele.kafkadistributedchatserver.security.auth.MemberPrincipalDetailService;
import leele.kafkadistributedchatserver.security.provider.MemberAuthenticatorProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    @Autowired
    MemberAuthenticatorProvider memberAuthenticatorProvider;

    @Autowired
    MemberPrincipalDetailService memberPrincipalDetailService;

    @Autowired
    public void configure (AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(memberAuthenticatorProvider);
    }

    @Bean
    public SecurityFilterChain memberSecurityFilterChain (HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize ->
                authorize
                    .requestMatchers("/css/**", "/images/**", "/js/**", "/auth/login", "/**")
                    .permitAll() // 해당 경로는 인증 없이 접근 가능
                    .requestMatchers("/auth/login/**") // 해당 경로는 인증이 필요
                    .hasRole("MEMBER")); // ROLE 이 MEMBER 가 포함된 경우에만 인증 가능
//            .formLogin(form ->
//                form
//                    .loginPage("/auth/login") // 로그인 페이지 설정
//                    .loginProcessingUrl("/auth/login") // 로그인 처리 URL 설정
//                    .defaultSuccessUrl("/main") // 로그인 성공 후 이동할 페이지
//                    .successHandler(new MemberAuthSuccessHandler()) // 로그인 성공 후 처리할 핸들러
//                    .failureHandler(new MemberAuthFailureHandler()) // 로그인 실패 후 처리할 핸들러
//                    .permitAll());

        http
            .logout(logout ->
                logout
                    .logoutUrl("/login/logout")
                    .logoutSuccessUrl("/login?logout=1")
                    .deleteCookies("JSESSIONID"));

        http.rememberMe(remember ->
            remember
                .key("leele") // 인증 토큰 생성시 사용할 키
                .tokenValiditySeconds(60 * 60 * 24 * 7) // 인증 토큰 유효 시간 (초)
                .userDetailsService(memberPrincipalDetailService) // 인증 토큰 생성시 사용할 UserDetailsService
                .rememberMeParameter("remember-me")); // 로그인 페이지에서 사용할 파라미터 이름

        return http.build();
    }
}
