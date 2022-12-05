package com.jongil.memo.domain.memo.exception;

import com.jongil.memo.domain.common.exception.EntityNotFoundException;

public class MemoNotFoundException extends EntityNotFoundException {
    public MemoNotFoundException() {
        super("게시물 조회 실패(이유:존재하지 않는 번호)", "게시물이 존재하지 않습니다.");
    }
}
