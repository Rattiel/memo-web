package com.jongil.memo.domain.memo;

import com.jongil.memo.domain.user.User;
import com.jongil.memo.global.config.security.Ownable;
import lombok.*;
import org.hibernate.annotations.Formula;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Memo implements Ownable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false, unique = true)
    private Long id;

    @Lob
    @Column(nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modifiedDate;

    @CreatedBy
    @JoinColumn(updatable = false, nullable = false)
    private String owner;

    @Formula("(SELECT p.id FROM Post p WHERE p.id > id ORDER BY p.id asc LIMIT 1)")
    private Long after;

    @Formula("(SELECT p.id FROM Post p WHERE p.id < id ORDER BY p.id desc LIMIT 1)")
    private Long before;

    public boolean isModified() {
        return createDate != modifiedDate;
    }

    public static Memo create(String content) {
        return Memo.builder()
                .content(content)
                .build();
    }

    public Memo update(String content) {
        this.content = content;

        return this;
    }
}
