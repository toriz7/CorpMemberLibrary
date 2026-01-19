package com.david.CorpMemberLibrary.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 사용자 데이터 접근 계층 (Repository)
 * Spring Data JPA를 사용하여 User 엔티티에 대한 데이터베이스 작업을 처리합니다.
 *
 * JpaRepository를 상속받아 기본적인 CRUD 메서드가 자동으로 제공됩니다:
 * - save(User entity): 사용자 저장/수정
 * - findById(String id): ID로 사용자 조회
 * - findAll(): 전체 사용자 조회
 * - delete(User entity): 사용자 삭제
 * - count(): 사용자 개수 조회
 */
public interface UserRepository extends JpaRepository<User, String> {

    /**
     * 사용자 ID(userId)로 사용자 조회
     *
     * @param userId 조회할 사용자 ID
     * @return Optional<User> 사용자가 존재하면 User 객체, 없으면 empty
     */
    Optional<User> findByUserId(String userId);

    /**
     * 사용자 ID(userId)가 이미 존재하는지 확인
     *
     * @param userId 확인할 사용자 ID
     * @return 존재하면 true, 없으면 false
     */
    boolean existsByUserId(String userId);
}
