package com.jongil.memo.domain.user.service;

import com.jongil.memo.domain.common.exception.FieldException;
import com.jongil.memo.domain.user.dto.UserRegisterParameter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    void register(UserRegisterParameter parameter) throws FieldException;
}
