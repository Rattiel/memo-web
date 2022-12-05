package com.jongil.memo.domain.common.exception;

import lombok.Getter;
import org.springframework.core.NestedRuntimeException;

@Getter
public class BusinessException extends NestedRuntimeException implements Reasonable {
    private final String reason;

    public BusinessException(String msg, String reason) {
        super(msg);
        this.reason = reason;
    }
}
