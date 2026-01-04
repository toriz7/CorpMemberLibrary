package com.david.CorpMemberLibrary.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handlePostNotFoundException(PostNotFoundException e, Model model) {
        log.error("PostNotFoundException: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleUserNotFoundException(UserNotFoundException e, Model model) {
        log.error("UserNotFoundException: {}", e.getMessage());
        model.addAttribute("error", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleBindException(BindException e, Model model) {
        log.error("Validation error: {}", e.getMessage());
        String errorMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .reduce((a, b) -> a + ", " + b)
                .orElse("입력값이 올바르지 않습니다.");

        model.addAttribute("error", errorMessage);
        return "error/400";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model) {
        log.error("Unexpected error: ", e);
        model.addAttribute("error", "서버 오류가 발생했습니다.");
        return "error/500";
    }
}
