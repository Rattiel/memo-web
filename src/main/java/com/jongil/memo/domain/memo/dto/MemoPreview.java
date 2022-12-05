package com.jongil.memo.domain.memo.dto;

import com.jongil.memo.domain.user.dto.UserInfo;

import java.time.LocalDateTime;
import java.util.List;

public interface MemoPreview {
    Long getId();
    UserInfo getOwner();
    LocalDateTime getCreateDate();
}
