package com.jongil.memo.global.config.jpa;

import com.jongil.memo.domain.user.User;
import com.jongil.memo.domain.user.dto.UserPrinciple;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/*
@Component
public class UserAuditorAware implements AuditorAware<User> {

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrinciple userPrinciple) {
            return Optional.of(userPrinciple.getUser());
        }

        throw new AccessDeniedException("잘못된 유저 정보입니다.");
    }
}
 */

@Component
public class UserAuditorAware implements AuditorAware<String> {
    @Transactional
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserPrinciple userPrinciple) {
            return Optional.of(userPrinciple.getUsername());
        }

        throw new AccessDeniedException("잘못된 유저 정보입니다.");
    }
}

