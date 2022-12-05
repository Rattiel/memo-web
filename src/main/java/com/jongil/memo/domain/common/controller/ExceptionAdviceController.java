package com.jongil.memo.domain.common.controller;

import com.jongil.memo.domain.memo.controller.MemoController;
import com.jongil.memo.domain.user.controller.UserController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice(
        assignableTypes = {
                BaseController.class,
                UserController.class,
                MemoController.class
        }
)
@Controller
public class ExceptionAdviceController {
    @ExceptionHandler({
            RuntimeException.class,
            Exception.class
    })
    public String internalServerError(Exception e) {
        log.error(e.getMessage());

        return "error/500";
    }

    @ExceptionHandler({
            DataAccessException.class
    })
    public String serviceUnavailable(Exception e) {
        log.error(e.getMessage());

        return "error/503";
    }
}