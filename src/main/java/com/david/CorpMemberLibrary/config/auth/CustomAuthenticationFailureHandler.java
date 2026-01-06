package com.david.CorpMemberLibrary.config.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 로그인 실패 시 처리를 담당하는 핸들러
 *
 * PENDING 상태 사용자(DisabledException)와 일반 로그인 실패를 구분하여
 * 적절한 에러 메시지를 표시합니다.
 */
@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage;

        if (exception instanceof DisabledException) {
            // 계정이 비활성화된 경우 (PENDING 또는 REJECTED 상태)
            errorMessage = "계정이 아직 승인되지 않았습니다. 관리자 승인을 기다려주세요.";
        } else {
            // 일반적인 로그인 실패 (아이디/비밀번호 오류)
            errorMessage = "아이디 또는 비밀번호가 올바르지 않습니다.";
        }

        // 에러 메시지를 URL 인코딩하여 전달
        String encodedMessage = URLEncoder.encode(errorMessage, StandardCharsets.UTF_8);
        setDefaultFailureUrl("/login?error=true&message=" + encodedMessage);

        super.onAuthenticationFailure(request, response, exception);
    }
}
