package com.david.CorpMemberLibrary.web;

import com.david.CorpMemberLibrary.dto.user.SignupRequestDto;
import com.david.CorpMemberLibrary.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * 회원가입 관련 웹 요청을 처리하는 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class SignupController {

    private final UserService userService;

    /**
     * 회원가입 페이지 표시
     *
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 뷰 이름 (templates/signup.html)
     */
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("signupRequest", new SignupRequestDto());
        return "signup";
    }

    /**
     * 회원가입 처리
     *
     * @param requestDto 회원가입 요청 DTO
     * @param bindingResult 유효성 검사 결과
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 뷰 이름
     */
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute("signupRequest") SignupRequestDto requestDto,
                         BindingResult bindingResult,
                         Model model) {

        // Bean Validation 오류 확인
        if (bindingResult.hasErrors()) {
            return "signup";
        }

        // 비밀번호 확인 불일치
        if (!requestDto.isPasswordMatching()) {
            bindingResult.rejectValue("passwordConfirm", "error.passwordConfirm",
                    "비밀번호가 일치하지 않습니다.");
            return "signup";
        }

        try {
            userService.signup(requestDto);
            // 성공 시 완료 페이지로 이동
            model.addAttribute("message", "회원가입이 완료되었습니다. 관리자 승인 후 로그인할 수 있습니다.");
            return "signup-complete";
        } catch (IllegalArgumentException e) {
            // 사번 중복 등의 오류
            bindingResult.rejectValue("userId", "error.userId", e.getMessage());
            return "signup";
        }
    }
}
