package com.jongil.memo.domain.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Builder
@Setter
@Getter
public class UserRegisterForm {
    @Pattern(regexp = "^[ㄱ-ㅎ|가-힣|a-z|A-Z|0-9|]{3,20}$", message = "올바른 이름 형식이 아닙니다.")
    private String nickname;

    @Pattern(regexp = "^[a-zA-Z0-9]{5,20}$", message = "올바른 아이디 형식이 아닙니다.")
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])(?=.*[#?!@$%^&*-]).{8,20}$", message = "올바른 비밀번호 형식이 아닙니다.")
    private String password;

    @Email(message = "올바른 이메일 형식 아닙니다.")
    private String email;
}

