package com.jongil.memo.domain.memo.dto;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemoForm {
    @NotEmpty(message = "본문을 입력해주세요.")
    private String content;

    public static MemoForm from(MemoData data) {
        return MemoForm.builder()
                .content(data.getContent())
                .build();
    }
}
