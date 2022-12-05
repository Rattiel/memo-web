package com.jongil.memo.domain.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class BaseController {
    @GetMapping("/")
    public String redirectIndex() {
        return "redirect:/post";
    }
}
