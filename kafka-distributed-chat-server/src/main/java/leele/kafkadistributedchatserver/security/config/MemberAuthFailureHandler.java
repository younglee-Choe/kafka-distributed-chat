package leele.kafkadistributedchatserver.security.config;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

public class MemberAuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.setAttribute("loginErrorMessage", exception.getMessage());
        setDefaultFailureUrl("/login?error=true&t=h");
        super.onAuthenticationFailure(request, response, exception);
    }
}
