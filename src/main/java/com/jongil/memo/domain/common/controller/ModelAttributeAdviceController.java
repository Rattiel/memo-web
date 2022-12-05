package com.jongil.memo.domain.common.controller;

import com.jongil.memo.domain.memo.controller.MemoController;
import com.jongil.memo.domain.user.controller.UserController;
import com.jongil.memo.global.config.session.GetSessionUser;
import com.jongil.memo.global.config.session.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@RequiredArgsConstructor
@ControllerAdvice(
        assignableTypes = {
                BaseController.class,
                BaseController.class,
                UserController.class,
                MemoController.class,
                ExceptionAdviceController.class
        }
)
@Controller
public class ModelAttributeAdviceController {
    @ModelAttribute
    public void bindMember(
            @GetSessionUser SessionUser user,
            Model model
    ) {
        if (user != null) {
            model.addAttribute("user", user);
        }
    }

    @GetMapping("/fragment")
    public String fragment() {
        return "fragment";
    }
}
