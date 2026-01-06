package com.david.CorpMemberLibrary.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 로그인 관련 웹 요청을 처리하는 컨트롤러
 * 
 * @Controller: Spring MVC에서 뷰를 반환하는 컨트롤러로 등록
 */
@Controller
public class LoginController {
    
    /**
     * 로그인 페이지 표시
     *
     * @param error 로그인 실패 시 "true" 값이 전달됨
     * @param message 커스텀 에러 메시지 (승인 대기 등)
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 뷰 이름 (templates/login.html)
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "message", required = false) String message,
                            Model model) {
        // 로그인 실패 시 에러 메시지 표시
        if (error != null && error.equals("true")) {
            if (message != null && !message.isEmpty()) {
                // CustomAuthenticationFailureHandler에서 전달한 메시지 사용
                model.addAttribute("error", message);
            } else {
                model.addAttribute("error", "아이디 또는 비밀번호가 올바르지 않습니다.");
            }
        }
        return "login";
    }
}

