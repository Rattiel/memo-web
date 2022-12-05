package com.jongil.memo;

import com.jongil.memo.domain.user.User;
import com.jongil.memo.domain.user.dto.UserRegisterForm;
import com.jongil.memo.domain.user.dto.UserRegisterParameter;
import com.jongil.memo.domain.user.repository.UserRepository;
import com.jongil.memo.global.config.security.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Profile("init")
@Slf4j
@RequiredArgsConstructor
@Component
public class DatabaseLoader implements ApplicationRunner {
    private final UserRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "1234";
    private static final String ADMIN_NICKNAME = "관리자";
    private static final String ADMIN_EMAIL = "whddlf0504@naver.com";

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        Optional<User> memberOptional = memberRepository.findByUsername(ADMIN_USERNAME);

        if (memberOptional.isPresent()) {
            log.info("기존 관리자 계정 (아이디 : {}, 비밀번호 : {})", ADMIN_USERNAME, "?");
        } else {
            UserRegisterForm adminMemberRegisterForm = UserRegisterForm.builder()
                    .username(ADMIN_USERNAME)
                    .password(ADMIN_PASSWORD)
                    .nickname(ADMIN_NICKNAME)
                    .email(ADMIN_EMAIL)
                    .build();

            User admin = User.register(UserRegisterParameter.from(adminMemberRegisterForm, UserRole.DEVELOPER), passwordEncoder);

            memberRepository.save(admin);

            log.info("새로 생성된 관리자 계정 (아이디 : {}, 비밀번호 : {})", ADMIN_USERNAME, ADMIN_PASSWORD);
        }
    }
}
