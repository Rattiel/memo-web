package com.jongil.memo.domain.common.exception;

import lombok.Getter;

@Getter
public class FieldException extends BusinessException implements Fieldable {
    private final String field;

    public FieldException(String msg, String field, String reason) {
        super(msg, reason);
        this.field = field;
    }
}
