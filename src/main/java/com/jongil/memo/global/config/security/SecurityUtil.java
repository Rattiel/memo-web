package com.jongil.memo.global.config.security;

import com.jongil.memo.domain.user.User;
import com.jongil.memo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SecurityUtil {
    private final UserRepository userRepository;

    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("사용자 정보 불러오기 실패(이유: 인증 안됌)");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails userDetails) {
            return userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> {throw new AccessDeniedException("사용자 정보 불러오기 실패(이유: 인증 안됌)");});
        }

        throw new ClassCastException("사용자 아이디 가져오기 실패(이유: UserDetails 로 형변환 실패)");
    }
}
