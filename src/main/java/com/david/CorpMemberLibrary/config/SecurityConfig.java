package com.david.CorpMemberLibrary.config;

import com.david.CorpMemberLibrary.config.auth.CustomAuthenticationFailureHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Spring Security 필터 체인 설정
     * 
     * HTTP 요청에 대한 보안 규칙을 정의합니다:
     * - 어떤 경로는 인증 없이 접근 가능
     * - 어떤 경로는 인증이 필요
     * - 로그인/로그아웃 경로 설정
     * 
     * @param http HttpSecurity 객체 (Spring Security가 제공)
     * @return SecurityFilterChain 설정된 보안 필터 체인
     * @throws Exception 설정 중 예외 발생 시
     */
    @Bean  // Spring: 이 메서드가 반환하는 객체를 빈으로 등록
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 사용자 인증 정보 제공자 설정
            // Spring Security가 자동으로 UserDetailsService 타입의 빈을 찾아서 사용합니다.
            // CustomUserDetailsService가 @Service로 등록되어 있으므로 자동으로 사용됩니다.
            
            // HTTP 요청에 대한 권한 설정
            .authorizeHttpRequests(auth -> auth
                // 루트 경로는 인증 없이 접근 가능 (로그인 상태 체크 후 리다이렉트)
                .requestMatchers("/").permitAll()
                // 회원가입 페이지와 처리 경로는 인증 없이 접근 가능
                .requestMatchers("/signup", "/signup/**").permitAll()
                // 로그인 페이지는 인증 없이 접근 가능
                .requestMatchers("/login", "/login/**").permitAll()
                // 관리자 페이지는 ADMIN 권한만 접근 가능
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 게시글 관련 경로는 인증이 필요 (로그인된 사용자만 접근 가능)
                // 그 외 모든 경로도 인증이 필요
                .anyRequest().authenticated()
            )

            // 로그인 설정
            .formLogin(form -> form
                .loginPage("/login")  // 커스텀 로그인 페이지 경로
                .defaultSuccessUrl("/posts", true)  // 로그인 성공 시 리다이렉트할 경로
                .failureHandler(authenticationFailureHandler)  // 커스텀 로그인 실패 핸들러
                .usernameParameter("username")  // 로그인 폼의 사용자명 필드명
                .passwordParameter("password")  // 로그인 폼의 비밀번호 필드명
                .permitAll()  // 로그인 페이지는 모든 사용자 접근 가능
            )
            
            // 로그아웃 설정
            .logout(logout -> logout
                .logoutUrl("/logout")  // 로그아웃 처리 경로
                .logoutSuccessUrl("/login")  // 로그아웃 성공 시 로그인 페이지로 리다이렉트
                .invalidateHttpSession(true)  // 세션 무효화
                .deleteCookies("JSESSIONID")  // 쿠키 삭제
                .permitAll()  // 로그아웃은 모든 사용자 접근 가능
            );

        return http.build();
    }
}

