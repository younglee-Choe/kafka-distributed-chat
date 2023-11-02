package leele.kafkadistributedchatserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import leele.kafkadistributedchatserver.member.entity.Member;
import leele.kafkadistributedchatserver.member.repository.MemberRepository;
import leele.kafkadistributedchatserver.security.auth.MemberPrincipalDetailService;
import leele.kafkadistributedchatserver.security.auth.MemberPrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class LoginController {
    private final MemberRepository memberRepository;

    @Autowired
    private MemberPrincipalDetailService memberPrincipalDetailService;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request,
                        @AuthenticationPrincipal MemberPrincipalDetails memberPrincipalDetails) {
        HttpSession session = request.getSession();
        String msg = (String) session.getAttribute("loginErrorMessage");
        session.setAttribute("loginErrorMessage", msg != null ? msg : "");

        if(isAuthenticated()) {
            if(memberPrincipalDetails == null)
                return "redirect:/login/logout";
            return "redirect:/main";
        }

        return "login";
    }

    @ResponseBody
    @PostMapping("/login")
    public ResponseEntity<Member> login(@RequestBody final Member params) {
        try {
            Member entity = memberRepository.findByMemberId(params.getMemberId());
            MemberPrincipalDetails memberPrincipalDetails = (MemberPrincipalDetails) memberPrincipalDetailService.loadUserByUsername(params.getMemberId());
            String dbPassword = memberPrincipalDetails.getPassword();
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            if(!passwordEncoder.matches(params.getPassword(), dbPassword)) {
                System.out.println("❌[User] Password does not match! " + params.getPassword() + ", " + dbPassword);
                return ResponseEntity.ok(null);
            } else {
                System.out.println("[User] Password matches! " + params.getPassword() + ", " + dbPassword);

                return ResponseEntity.ok(Member.builder()
                        .memberId(entity.getMemberId())
                        .memberName(entity.getMemberName())
                        .build());
            }
        } catch (Exception e) {
             System.out.println("❗️An exception occurred during the login process: " + e);
        }
        return ResponseEntity.ok(null);
    }

//    @GetMapping("/main")
//    public String main() {
//        return "main";
//    }
//
//    @GetMapping("/text")
//    public String text(@AuthenticationPrincipal MemberPrincipalDetails memberPrincipalDetails
//                        ,Model model) {
//
//        Member member = memberPrincipalDetails.getMember();
//
//        model.addAttribute("member", member);
//        return "text";
//    }
}
