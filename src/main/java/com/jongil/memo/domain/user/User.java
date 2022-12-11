package com.jongil.memo.domain.user;

import com.jongil.memo.domain.user.dto.UserInfo;
import com.jongil.memo.domain.user.dto.UserPrincipal;
import com.jongil.memo.domain.user.dto.UserRegisterParameter;
import com.jongil.memo.global.config.security.UserRole;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="USER_DATA")
public class User implements UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    public static User register(UserRegisterParameter parameter, PasswordEncoder passwordEncoder) {
        return User.builder()
                    .username(parameter.getUsername())
                    .password(passwordEncoder.encode(parameter.getPassword()))
                    .nickname(parameter.getNickname())
                    .email(parameter.getEmail())
                    .role(parameter.getRole())
                .build();
    }

    public static User from(UserPrincipal principle) {
        return User.builder()
                .id(principle.getId())
                .username(principle.getUsername())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
