package com.david.CorpMemberLibrary.service.user;

import com.david.CorpMemberLibrary.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring Security의 UserDetailsService 구현 클래스
 * 
 * Spring Security가 사용자 인증을 수행할 때 이 클래스를 사용하여
 * 사용자 정보를 조회합니다.
 * 
 * @Service: Spring이 이 클래스를 서비스 빈으로 등록
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성 (의존성 주입용)
 */
@Service  // Spring: 이 클래스를 서비스 빈으로 등록하여 의존성 주입 가능하게 함ㅡ
@RequiredArgsConstructor  // Lombok: final 필드에 대한 생성자 자동 생성 (의존성 주입)
public class CustomUserDetailsService implements UserDetailsService {
    
    /**
     * 사용자 비즈니스 로직 서비스
     * 
     * @RequiredArgsConstructor로 자동 생성된 생성자를 통해
     * Spring이 UserService를 주입해줍니다 (의존성 주입, DI)
     */
    private final UserService userService;
    
    /**
     * 사용자명으로 사용자 정보를 조회하는 메서드
     * 
     * Spring Security가 로그인 시 이 메서드를 호출하여
     * 사용자 정보를 조회하고 비밀번호를 검증합니다.
     * 
     * @param username 로그인 시 입력한 사용자명
     * @return UserDetails 사용자 인증 정보 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // UserService를 통해 데이터베이스에서 사용자 조회
        User user;
        try {
            user = userService.findByUsername(username);
        } catch (IllegalArgumentException e) {
            // UserService가 던진 IllegalArgumentException을 UsernameNotFoundException으로 변환
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username, e);
        }
        
        // 사용자의 권한(role)을 GrantedAuthority 리스트로 변환
        // Spring Security는 권한을 GrantedAuthority 객체로 관리합니다.
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
        // "ROLE_" 접두사는 Spring Security의 권한 체계에 맞추기 위함입니다.
        // 예: "USER" -> "ROLE_USER", "ADMIN" -> "ROLE_ADMIN"
        
        // Spring Security의 UserDetails 구현체를 생성하여 반환
        // 이 객체에는 사용자명, 암호화된 비밀번호, 권한 정보가 포함됩니다.
        // Spring Security가 이 비밀번호와 사용자가 입력한 비밀번호를 비교하여 인증을 수행합니다.
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())  // 사용자명
                .password(user.getPassword())  // 암호화된 비밀번호 (BCrypt)
                .authorities(authorities)  // 사용자 권한 목록
                .build();
    }
}

