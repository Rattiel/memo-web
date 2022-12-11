package com.jongil.memo.domain.memo.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jongil.memo.global.config.security.Ownable;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class MemoId implements Ownable {
    private Long id;
    @JsonIgnore
    private String principalName;
}
