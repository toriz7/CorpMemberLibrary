package com.david.CorpMemberLibrary.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${spring.h2.console.enabled:false}")
    private boolean h2ConsoleEnabled;
    
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
                // H2 콘솔은 인증 없이 접근 가능 (개발 환경용)
                .requestMatchers("/h2-console/**").permitAll()
                // 회원가입 페이지와 처리 경로는 인증 없이 접근 가능
                .requestMatchers("/signup", "/signup/**").permitAll()
                // 로그인 페이지는 인증 없이 접근 가능
                .requestMatchers("/login", "/login/**").permitAll()
                // 게시글 관련 경로는 인증이 필요 (로그인된 사용자만 접근 가능)
                // 그 외 모든 경로도 인증이 필요
                .anyRequest().authenticated()
            )
            
            // 로그인 설정
            .formLogin(form -> form
                .loginPage("/login")  // 커스텀 로그인 페이지 경로
                .defaultSuccessUrl("/posts", true)  // 로그인 성공 시 리다이렉트할 경로
                .failureUrl("/login?error=true")  // 로그인 실패 시 리다이렉트할 경로
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
            )
            
            // CSRF 보호 설정
            .csrf(csrf -> {
                if (h2ConsoleEnabled) {
                    // 개발 환경: H2 콘솔을 위해 CSRF 비활성화
                    csrf.ignoringRequestMatchers("/h2-console/**");
                }
            })

            // H2 콘솔 사용을 위한 프레임 옵션 설정
            .headers(headers -> {
                if (h2ConsoleEnabled) {
                    // 개발 환경: H2 콘솔을 위해 프레임 옵션 허용
                    headers.frameOptions(frame -> frame.sameOrigin());
                }
            });
        
        return http.build();
    }
}

