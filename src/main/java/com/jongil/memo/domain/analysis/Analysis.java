package com.jongil.memo.domain.analysis;

import com.jongil.memo.domain.user.User;
import com.jongil.memo.global.config.security.Ownable;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Analysis implements Ownable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private User owner;

    @Column
    private Integer score;

    @CreatedBy
    @JoinColumn
    private String principalName;

    public static Analysis create(User user) {
        return Analysis.builder()
                .owner(user)
                .score(0)
                .build();
    }

    public Analysis update(int score) {
        this.score += score;
        return this;
    }

    public Analysis reset() {
        this.score = 0;
        return this;
    }
}
