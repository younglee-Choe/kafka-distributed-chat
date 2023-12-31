package leele.kafkadistributedchatserver.controller;

import leele.kafkadistributedchatserver.member.entity.Member;
import leele.kafkadistributedchatserver.security.auth.MemberPrincipalDetails;
import leele.kafkadistributedchatserver.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class SignupController {
    private final SignupService signupService;

    @ResponseBody
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody final Member params) {
        Member result = signupService.register(params);

        if (result == null) {
            return  ResponseEntity.ok("This ID already exists");
        } else {
            return ResponseEntity.ok("Successfully registered as a member");
        }
    }
}
