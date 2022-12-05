package com.jongil.memo.global.config.security;

import lombok.Getter;

@Getter
public enum UserRole {
    DEVELOPER("ROLE_DEVELOPER", "개발자"),
    ADMIN("ROLE_ADMIN", "관리자"),
    USER("ROLE_USER", "사용자");

    UserRole(String id, String name) {
        this.id = id;
        this.name = name;
    }

    private final String id;
    private final String name;
}
