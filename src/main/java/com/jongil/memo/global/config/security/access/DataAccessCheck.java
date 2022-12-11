package com.jongil.memo.global.config.security.access;

import com.jongil.memo.global.config.security.Ownable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Aspect
public class DataAccessCheck {
    @Around("@annotation(AccessOnlyOwner)")
    public Object accessOnlyOwner(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = proceedingJoinPoint.proceed();

        return ownerCheck(object);
    }

    @Around("@annotation(AccessOnlyAdmin)")
    public Object accessOnlyAdmin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = proceedingJoinPoint.proceed();

        if (isAdmin()) {
            return object;
        }

        throw new AccessDeniedException("권한 체크 실패(이유: 관리자 아님)");
    }

    @Around("@annotation(AccessOwnerAndAdmin)")
    public Object accessOwnerAndAdmin(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object object = proceedingJoinPoint.proceed();

        if (isAdmin()) {
            return object;
        }

        return ownerCheck(object);
    }

    private String getPrincipalName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Object principal = authentication.getPrincipal();

        if (authentication.isAuthenticated()) {
            if (principal instanceof UserDetails userDetails) {
                return userDetails.getUsername();
            }
        }

        throw new AccessDeniedException("권한 체크 실패(이유: 인증 안됌)");
    }

    private Object ownerCheck(Object object) {
        String principalName = getPrincipalName();

        if (object == null) {
            return null;
        } else if (object instanceof Optional<?> optional) {
            Ownable data = getOptional(optional);

            if (data == null) {
                return object;
            }

            if (principalName.equals(data.getPrincipalName())) {
                return object;
            }
        } else if (object instanceof Ownable data) {
            if (principalName.equals(data.getPrincipalName())) {
                return object;
            }
        } else if (object instanceof List<?> list) {
            for (Object item: list) {
                if (item instanceof Ownable ownable) {
                    if (!principalName.equals(ownable.getPrincipalName())) {
                        throw new AccessDeniedException("권한 체크 실패(이유: 소유자 또는 관리자 아님)");
                    }
                }
            }

            return object;
        }

        throw new AccessDeniedException("권한 체크 실패(이유: 인증 안됌)");
    }

    private Ownable getOptional(Object object) {
        if (object instanceof Optional<?> optional) {
            if (optional.isPresent()) {
                if (optional.get() instanceof Ownable data) {
                    return data;
                }
            } else {
                return null;
            }
        }

        throw new ClassCastException("권한 체크 실패(이유: 올바르지 않은 데이터 타입)");
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
    }
}
