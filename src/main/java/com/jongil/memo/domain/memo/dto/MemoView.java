package com.jongil.memo.domain.memo.dto;

import com.jongil.memo.global.config.security.Ownable;

import java.time.LocalDateTime;

public interface MemoView extends Ownable {
    Long getId();

    String getTitle();

    String getContent();

    LocalDateTime getCreateDate();
}
