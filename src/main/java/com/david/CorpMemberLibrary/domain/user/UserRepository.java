package com.david.CorpMemberLibrary.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * 사용자 데이터 접근 계층 (Repository)
 * Spring Data JPA를 사용하여 User 엔티티에 대한 데이터베이스 작업을 처리합니다.
 * 
 * JpaRepository를 상속받아 기본적인 CRUD 메서드가 자동으로 제공됩니다:
 * - save(User entity): 사용자 저장/수정
 * - findById(Long id): ID로 사용자 조회
 * - findAll(): 전체 사용자 조회
 * - delete(User entity): 사용자 삭제
 * - count(): 사용자 개수 조회
 * 등
 * 
 * 사용 예시:
 * @Autowired
 * private UserRepository;
 * 
 * User = User.builder()
 *     .username("testuser")
 *     .password("encryptedPassword")
 *     .name("홍길동")
 *     .position("대리")
 *     .build();
 * userRepository.save(user);
 */
public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository<User, Long>
    // - User: 엔티티 타입
    // - Long: 엔티티의 ID 타입

    /**
     * 사용자 ID(userId)로 사용자 조회
     * Spring Data JPA가 메서드 이름을 분석하여 자동으로 쿼리를 생성합니다.
     * 
     * @param userId 조회할 사용자 ID
     * @return Optional<User> 사용자가 존재하면 User 객체, 없으면 empty
     * 
     * 생성되는 쿼리:
     * SELECT * FROM users WHERE user_id = ?
     */
    Optional<User> findByUserId(String userId);

    /**
     * 사용자 ID(userId)가 이미 존재하는지 확인
     *
     * @param userId 확인할 사용자 ID
     * @return 존재하면 true, 없으면 false
     *
     * 생성되는 쿼리:
     * SELECT COUNT(*) > 0 FROM users WHERE user_id = ?
     */
    boolean existsByUserId(String userId);

    /**
     * 특정 상태의 사용자 목록 조회
     *
     * @param status 조회할 사용자 상태 (PENDING, APPROVED, REJECTED)
     * @return 해당 상태의 사용자 목록
     *
     * 생성되는 쿼리:
     * SELECT * FROM users WHERE status = ?
     */
    List<User> findByStatus(UserStatus status);

    /**
     * 특정 상태의 사용자 목록을 가입일 역순으로 조회
     *
     * @param status 조회할 사용자 상태
     * @return 해당 상태의 사용자 목록 (최신 가입순)
     *
     * 생성되는 쿼리:
     * SELECT * FROM users WHERE status = ? ORDER BY created_date DESC
     */
    List<User> findByStatusOrderByCreatedDateDesc(UserStatus status);

    /**
     * 특정 상태의 사용자 수 조회
     *
     * @param status 조회할 사용자 상태
     * @return 해당 상태의 사용자 수
     *
     * 생성되는 쿼리:
     * SELECT COUNT(*) FROM users WHERE status = ?
     */
    long countByStatus(UserStatus status);
}

