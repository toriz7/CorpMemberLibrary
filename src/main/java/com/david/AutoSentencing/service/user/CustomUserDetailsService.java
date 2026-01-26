package com.david.AutoSentencing.service.user;

import com.david.AutoSentencing.domain.user.User;
import com.david.AutoSentencing.domain.user.UserRepository;
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
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * 사용자명으로 사용자 정보를 조회하는 메서드
     *
     * Spring Security가 로그인 시 이 메서드를 호출하여
     * 사용자 정보를 조회하고 비밀번호를 검증합니다.
     *
     * @param username 로그인 시 입력한 사용자명 (사번)
     * @return UserDetails 사용자 인증 정보 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // PENDING 상태인 사용자는 로그인 차단
        if ("PENDING".equals(user.getStatus())) {
            throw new UsernameNotFoundException("승인 대기 중인 계정입니다. 관리자 승인 후 로그인이 가능합니다.");
        }

        // 사용자의 권한(role)을 GrantedAuthority 리스트로 변환
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // Spring Security의 UserDetails 구현체를 생성하여 반환
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
