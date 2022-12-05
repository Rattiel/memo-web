package com.jongil.memo.domain.user.dto;

import com.jongil.memo.global.config.security.UserRole;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Builder(access = AccessLevel.PRIVATE)
public class UserRegisterParameter {
    private String nickname;

    private String username;

    private String password;

    private String email;

    private UserRole role;

    public static UserRegisterParameter from(UserRegisterForm form) {

        return UserRegisterParameter.builder()
                    .nickname(form.getNickname())
                    .username(form.getUsername())
                    .password(form.getPassword())
                    .email(form.getEmail())
                    .role(UserRole.USER)
                .build();
    }

    public static UserRegisterParameter from(UserRegisterForm form, UserRole role) {
        return UserRegisterParameter.builder()
                    .nickname(form.getNickname())
                    .username(form.getUsername())
                    .password(form.getPassword())
                    .email(form.getEmail())
                    .role(role)
                .build();
    }
}
