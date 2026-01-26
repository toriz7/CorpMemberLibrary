package com.david.AutoSentencing.service.user;

import com.david.AutoSentencing.domain.user.User;
import com.david.AutoSentencing.domain.user.UserRepository;
import com.david.AutoSentencing.dto.user.SignupRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 사용자 비즈니스 로직을 처리하는 서비스 계층
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 회원가입 메서드 (DTO 기반)
     *
     * @param requestDto 회원가입 요청 DTO
     * @return 저장된 사용자의 ID
     * @throws IllegalArgumentException 사번이 이미 존재하거나 비밀번호가 일치하지 않을 때
     */
    @Transactional
    public String signup(SignupRequestDto requestDto) {
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

        // Entity 생성 및 저장
        User user = requestDto.toEntity(encodedPassword);
        User savedUser = userRepository.save(user);

        return savedUser.getUserId();
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
     * 사용자 ID로 사용자 조회 메서드
     *
     * @param userId 조회할 사용자 ID
     * @return User 엔티티
     * @throws IllegalArgumentException 사용자가 존재하지 않을 때
     */
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 사용자가 없습니다. userId=" + userId));
    }

    /**
     * 사용자 ID(PK)로 사용자 조회 메서드
     *
     * @param id 조회할 사용자 ID (PK)
     * @return User 엔티티
     * @throws IllegalArgumentException 사용자가 존재하지 않을 때
     */
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 사용자가 없습니다. id=" + id));
    }

    /**
     * 승인 대기 중인 사용자 목록 조회
     *
     * @return PENDING 상태의 사용자 목록
     */
    public List<User> findPendingUsers() {
        return userRepository.findByStatus("PENDING");
    }

    /**
     * 승인 대기 중인 사용자 수 조회
     *
     * @return PENDING 상태의 사용자 수
     */
    public long countPendingUsers() {
        return userRepository.countByStatus("PENDING");
    }

    /**
     * 사용자 승인 처리
     *
     * @param userId 승인할 사용자 ID
     */
    @Transactional
    public void approveUser(String userId) {
        User user = findById(userId);
        user.updateStatus("APPROVED");
    }

    /**
     * 사용자 거부 처리
     *
     * @param userId 거부할 사용자 ID
     */
    @Transactional
    public void rejectUser(String userId) {
        User user = findById(userId);
        user.updateStatus("REJECTED");
    }
}
