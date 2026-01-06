package com.david.CorpMemberLibrary.service.user;

import com.david.CorpMemberLibrary.domain.user.User;
import com.david.CorpMemberLibrary.domain.user.UserRepository;
import com.david.CorpMemberLibrary.domain.user.UserStatus;
import com.david.CorpMemberLibrary.dto.user.SignupRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * 회원가입 메서드 (DTO 기반)
     * 회원가입 시 PENDING 상태로 저장되며, 관리자 승인 후 로그인이 가능합니다.
     *
     * @param requestDto 회원가입 요청 DTO
     * @return 저장된 사용자의 ID
     * @throws IllegalArgumentException 사번이 이미 존재하거나 비밀번호가 일치하지 않을 때
     */
    @Transactional
    public Long signup(SignupRequestDto requestDto) {
        // 사번 중복 확인
        if (userRepository.existsByUserId(requestDto.getUserId())) {
            throw new IllegalArgumentException("이미 존재하는 사번입니다: " + requestDto.getUserId());
        }

        // 비밀번호 확인 일치 검증
        if (!requestDto.isPasswordMatching()) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        // PENDING 상태로 Entity 생성 및 저장
        User user = requestDto.toEntity(encodedPassword);
        User savedUser = userRepository.save(user);

        return savedUser.getId();
    }

    /**
     * 승인 대기 중인 사용자 목록 조회
     *
     * @return 승인 대기 중인 사용자 목록 (최신 가입순)
     */
    public List<User> findPendingUsers() {
        return userRepository.findByStatusOrderByCreatedDateDesc(UserStatus.PENDING);
    }

    /**
     * 전체 사용자 목록 조회 (관리자용)
     *
     * @return 전체 사용자 목록
     */
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 사용자 승인 처리
     *
     * @param userId 승인할 사용자의 ID
     * @throws IllegalArgumentException 사용자가 없을 때
     * @throws IllegalStateException 승인 대기 상태가 아닐 때
     */
    @Transactional
    public void approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + userId));

        if (user.getStatus() != UserStatus.PENDING) {
            throw new IllegalStateException("승인 대기 상태의 사용자만 승인할 수 있습니다.");
        }

        user.approve();
    }

    /**
     * 사용자 승인 거부 처리
     *
     * @param userId 거부할 사용자의 ID
     * @throws IllegalArgumentException 사용자가 없을 때
     * @throws IllegalStateException 승인 대기 상태가 아닐 때
     */
    @Transactional
    public void rejectUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다. id=" + userId));

        if (user.getStatus() != UserStatus.PENDING) {
            throw new IllegalStateException("승인 대기 상태의 사용자만 거부할 수 있습니다.");
        }

        user.reject();
    }

    /**
     * 승인 대기 사용자 수 조회
     *
     * @return 승인 대기 중인 사용자 수
     */
    public long countPendingUsers() {
        return userRepository.countByStatus(UserStatus.PENDING);
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

