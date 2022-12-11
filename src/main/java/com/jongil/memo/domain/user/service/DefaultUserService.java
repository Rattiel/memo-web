package com.jongil.memo.domain.user.service;

import com.jongil.memo.domain.common.exception.FieldException;
import com.jongil.memo.domain.user.User;
import com.jongil.memo.domain.user.dto.UserPrincipal;
import com.jongil.memo.domain.user.dto.UserRegisterParameter;
import com.jongil.memo.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class DefaultUserService implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(username)
        );

        return UserPrincipal.from(user);
    }

    @Transactional
    @Override
    public void register(UserRegisterParameter parameter) throws FieldException {
        check(
                parameter.getUsername(),
                parameter.getEmail()
        );

        userRepository.save(User.register(parameter, passwordEncoder));
    }

    private void check(String username, String email) {
        usernameCheck(username);
        emailCheck(email);
    }

    private void usernameCheck(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new FieldException("아이디 체크 실패(이유: 중복)", "username", "이미 사용중인 아이디 입니다.");
        }
    }

    private void emailCheck(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new FieldException("이메일 체크 실패(이유: 중복)", "email", "이미 사용중인 이메일 입니다.");
        }
    }
}
