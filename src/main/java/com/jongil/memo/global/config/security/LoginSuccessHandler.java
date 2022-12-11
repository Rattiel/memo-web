package com.jongil.memo.global.config.security;

import com.jongil.memo.domain.user.dto.UserPrincipal;
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

@Slf4j
@RequiredArgsConstructor
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final HttpSession httpSession;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrincipal userPrincipal) {
            httpSession.setAttribute(SessionUser.ATTRIBUTE_NAME, SessionUser.of(userPrincipal));
            super.onAuthenticationSuccess(request, response, authentication);
        } else {
            log.error("올바르지 않은 principal 타입");
            throw new RuntimeException("세션유저 등록 실패(이유: 올바르지 않은 principal 타입)");
        }
    }
}


