package com.david.CorpMemberLibrary.service.user;

import com.david.CorpMemberLibrary.domain.user.User;
import com.david.CorpMemberLibrary.domain.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 사용자 비즈니스 로직을 처리하는 서비스 계층
 * 
 * Service 계층의 역할:
 * 1. 비즈니스 로직 처리
 * 2. 트랜잭션 관리
 * 3. Repository와 Controller 사이의 중간 계층
 * 4. 비밀번호 암호화 처리
 * 
 * @Service: Spring이 이 클래스를 서비스 빈으로 등록
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성 (의존성 주입용)
 */
@Service  // Spring: 이 클래스를 서비스 빈으로 등록하여 의존성 주입 가능하게 함
@RequiredArgsConstructor  // Lombok: final 필드에 대한 생성자 자동 생성 (의존성 주입)
public class UserService {
    
    /**
     * 사용자 데이터 접근 계층 (Repository)
     * 
     * @RequiredArgsConstructor로 자동 생성된 생성자를 통해
     * Spring이 UserRepository를 주입해줍니다 (의존성 주입, DI)
     */
    private final UserRepository userRepository;
    
    /**
     * 비밀번호 암호화를 위한 PasswordEncoder
     * BCryptPasswordEncoder를 사용하여 비밀번호를 암호화합니다.
     * 
     * SecurityConfig에서 @Bean으로 등록된 PasswordEncoder가 주입됩니다.
     */
    private final PasswordEncoder passwordEncoder;
    
    /**
     * 회원가입 메서드
     * 
     * @Transactional 어노테이션:
     * - 이 메서드가 실행되는 동안 하나의 트랜잭션으로 묶임
     * - 메서드 실행 중 예외 발생 시 모든 변경사항이 롤백됨
     * - 데이터 일관성 보장
     * 
     * @param userId 사용자명 (로그인 아이디)
     * @param password 평문 비밀번호 (암호화되어 저장됨)
     * @param name 사용자 실명
     * @param position 사용자 직책
     * @return 저장된 사용자의 ID
     * @throws IllegalArgumentException 사용자명이 이미 존재할 때
     */
    @Transactional  // Spring: 이 메서드를 트랜잭션으로 묶음
    public Long signup(String userId, String password, String name, String position) {
        // 사용자 ID 중복 확인
        if (userRepository.existsByUserId(userId)) {
            throw new IllegalArgumentException("이미 존재하는 사용자 ID입니다: " + userId);
        }
        
        // 평문 비밀번호를 BCrypt로 암호화
        // BCrypt는 단방향 해시 함수로, 원본 비밀번호를 복원할 수 없습니다.
        // 같은 비밀번호라도 매번 다른 해시 값이 생성됩니다 (솔트 자동 생성)
        String encodedPassword = passwordEncoder.encode(password);
        
        // DTO를 Entity로 변환
        // Builder 패턴을 사용하여 Entity 생성
        User user = User.builder()
                .userId(userId)  // 사용자ID
                .password(encodedPassword)  // 암호화된 비밀번호 저장
                .name(name)  // 실명
                .position(position)  // 직책
                .role("USER")  // 기본 권한은 "USER"
                .build();  // User 객체 생성
        
        // Repository를 통해 데이터베이스에 저장
        // save() 메서드는 저장된 Entity를 반환
        User savedUser = userRepository.save(user);
        
        // 저장된 사용자의 ID를 반환
        return savedUser.getId();
    }
    
    /**
     * 사용자 ID로 사용자 조회 메서드
     * 
     * @param userId 조회할 사용자 ID
     * @return User 엔티티
     * @throws IllegalArgumentException 사용자가 존재하지 않을 때
     */
    public User findByUserId(String userId) {
        // 데이터베이스에서 사용자 조회
        // findByUserId()는 Optional<User>를 반환하므로 orElseThrow()로 예외 처리
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 사용자가 없습니다. userId=" + userId));
    }
    
    /**
     * 사용자 ID로 사용자 조회 메서드
     * 
     * @param id 조회할 사용자 ID
     * @return User 엔티티
     * @throws IllegalArgumentException 사용자가 존재하지 않을 때
     */
    public User findById(Long id) {
        // 데이터베이스에서 사용자 조회
        // findById()는 Optional<User>를 반환하므로 orElseThrow()로 예외 처리
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 사용자가 없습니다. id=" + id));
    }
}

