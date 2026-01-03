package com.david.CorpMemberLibrary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security 설정 클래스
 * 
 * @Configuration: Spring이 이 클래스를 설정 클래스로 인식
 * @EnableWebSecurity: Spring Security 웹 보안 기능 활성화
 */
@Configuration  // Spring: 이 클래스를 설정 클래스로 등록
@EnableWebSecurity  // Spring Security: 웹 보안 기능 활성화
public class SecurityConfig {
    
    /**
     * 비밀번호 암호화를 위한 PasswordEncoder 빈 등록
     * 
     * BCryptPasswordEncoder는 BCrypt 해시 알고리즘을 사용하여 비밀번호를 암호화합니다.
     * - 기본 강도(strength)는 10입니다.
     * - 같은 비밀번호라도 매번 다른 해시 값이 생성됩니다 (솔트 자동 생성).
     * - 단방향 해시 함수로, 원본 비밀번호를 복원할 수 없습니다.
     * 
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean  // Spring: 이 메서드가 반환하는 객체를 빈으로 등록
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
        // 더 강한 암호화를 원하면: new BCryptPasswordEncoder(12)
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
            // H2 콘솔 사용을 위해 일시적으로 비활성화 (프로덕션에서는 활성화해야 함)
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            
            // H2 콘솔 사용을 위한 프레임 옵션 설정
            .headers(headers -> headers
                .frameOptions(frame -> frame.sameOrigin())
            );
        
        return http.build();
    }
}

