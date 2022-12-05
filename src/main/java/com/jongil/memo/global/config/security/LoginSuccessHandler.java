package com.jongil.memo.global.config.security;

import com.jongil.memo.domain.user.User;
import com.jongil.memo.domain.user.repository.UserRepository;
import com.jongil.memo.global.config.session.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UserRepository userRepository;

    private final HttpSession httpSession;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();

        if (principal instanceof org.springframework.security.core.userdetails.User user) {
            Optional<User> optionalUser = userRepository.findByUsername(
                    user.getUsername()
            );

            if (optionalUser.isPresent()) {
                httpSession.setAttribute(SessionUser.ATTRIBUTE_NAME, SessionUser.of(optionalUser.get()));
                super.onAuthenticationSuccess(request, response, authentication);
                return;
            }
        }

        throw new RuntimeException("세션 유저 등록 실패!");
    }
}


