package com.jongil.memo.domain.memo;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createDate;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(updatable = false, nullable = false)
    private User writer;

    @JsonIgnore
    @CreatedBy
    @JoinColumn(updatable = false, nullable = false)
    private String principalName;

    public static Memo create(String title, User writer, String content) {
        return Memo.builder()
                .title(title)
                .writer(writer)
                .content(content)
                .build();
    }

    public Memo update(String title, String content) {
        this.title = title;
        this.content = content;

        return this;
    }
}
