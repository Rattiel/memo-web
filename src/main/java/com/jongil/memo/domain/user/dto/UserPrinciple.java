package com.jongil.memo.domain.user.dto;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.Collections;

@Getter
public class UserPrinciple extends User {
    private Long id;
    private String nickname;

    public UserPrinciple(
            String username,
            String password,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, authorities);
    }

    public UserPrinciple(
            String username,
            String password,
            boolean enabled,
            boolean accountNonExpired,
            boolean credentialsNonExpired,
            boolean accountNonLocked,
            Collection<? extends GrantedAuthority> authorities
    ) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public static UserPrinciple from(com.jongil.memo.domain.user.User user) {
        UserPrinciple userPrinciple = new UserPrinciple(
                user.getUsername(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole().getId()))
        );

        userPrinciple.id = user.getId();
        userPrinciple.nickname = user.getNickname();

        return userPrinciple;
    }
}
