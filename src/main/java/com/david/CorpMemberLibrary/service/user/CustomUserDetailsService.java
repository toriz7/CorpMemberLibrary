package com.david.CorpMemberLibrary.service.user;

import com.david.CorpMemberLibrary.domain.user.User;
import com.david.CorpMemberLibrary.domain.user.UserRepository;
import com.david.CorpMemberLibrary.domain.user.UserStatus;
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
@Service  // Spring: 이 클래스를 서비스 빈으로 등록하여 의존성 주입 가능하게 함
@RequiredArgsConstructor  // Lombok: final 필드에 대한 생성자 자동 생성 (의존성 주입)
public class CustomUserDetailsService implements UserDetailsService {

    /**
     * 사용자 데이터 접근 계층 (Repository)
     * 순환 참조 방지를 위해 UserService 대신 UserRepository를 직접 사용합니다.
     */
    private final UserRepository userRepository;

    /**
     * 사용자명으로 사용자 정보를 조회하는 메서드
     *
     * Spring Security가 로그인 시 이 메서드를 호출하여
     * 사용자 정보를 조회하고 비밀번호를 검증합니다.
     *
     * PENDING 또는 REJECTED 상태의 사용자는 disabled 처리되어
     * 로그인이 차단됩니다.
     *
     * @param username 로그인 시 입력한 사용자명 (사번)
     * @return UserDetails 사용자 인증 정보 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + username));

        // 사용자의 권한(role)을 GrantedAuthority 리스트로 변환
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        // APPROVED 상태만 활성화, 나머지(PENDING, REJECTED)는 비활성화
        boolean isEnabled = user.getStatus() == UserStatus.APPROVED;

        // Spring Security의 UserDetails 구현체를 생성하여 반환
        // disabled가 true이면 DisabledException이 발생하여 로그인 차단
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUserId())
                .password(user.getPassword())
                .authorities(authorities)
                .disabled(!isEnabled)  // APPROVED가 아니면 비활성화
                .build();
    }
}

