package com.jongil.memo.domain.analysis.controller;

import com.jongil.memo.domain.analysis.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class AnalysisController {
    private final AnalysisService analysisService;

    @SubscribeMapping("/user/message")
    public void onSubscribe(SimpMessageHeaderAccessor accessor) {
        String username = accessor.getUser().getName();
        analysisService.check(username);
    }
}
