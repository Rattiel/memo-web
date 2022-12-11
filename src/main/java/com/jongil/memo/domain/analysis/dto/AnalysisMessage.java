package com.jongil.memo.domain.analysis.dto;

import com.jongil.memo.global.config.security.Ownable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnalysisMessage implements Ownable {
    private String principalName;

    private String message;
}
