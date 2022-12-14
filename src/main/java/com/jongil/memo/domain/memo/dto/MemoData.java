package com.jongil.memo.domain.memo.dto;

import com.jongil.memo.global.config.security.Ownable;

public interface MemoData extends Ownable {
    String getTitle();

    String getContent();
}
