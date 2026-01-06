package com.david.CorpMemberLibrary.domain.user;

import com.david.CorpMemberLibrary.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 사용자 엔티티 클래스
 * JPA를 사용하여 데이터베이스의 users 테이블과 매핑됩니다.
 */
@Getter  // Lombok: 모든 필드에 대한 getter 메서드 자동 생성
@NoArgsConstructor  // Lombok: 기본 생성자 자동 생성 (JPA 필수)
@Entity  // JPA: 이 클래스를 엔티티로 지정
@Table(name = "users")  // JPA: 데이터베이스 테이블명을 "users"로 지정
public class User extends BaseTimeEntity {

    /**
     * 사용자 고유 ID (기본키)
     * 데이터베이스에서 자동으로 증가하는 값으로 생성됩니다.
     */
    @Id  // JPA: 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // JPA: ID 자동 생성 전략 (AUTO_INCREMENT)
    private Long id;

    /**
     * 사용자 로그인 아이디
     * 중복 불가, 필수 입력 항목입니다.
     */
    @Column(unique = true, nullable = false, length = 50)  // JPA: UNIQUE, NOT NULL, 최대 50자
    private String userId;

    /**
     * 사용자 비밀번호
     * BCrypt로 암호화되어 저장되며, 필수 입력 항목입니다.
     */
    @Column(nullable = false)  // JPA: NOT NULL 제약조건
    private String password;

    /**
     * 사용자 실명
     * 선택 입력 항목입니다.
     */
    @Column(length = 50)  // JPA: 최대 50자
    private String name;

    /**
     * 사용자 직책
     * 선택 입력 항목입니다.
     */
    @Column(length = 50)  // JPA: 최대 50자
    private String position;

    /**
     * 사용자 권한
     * 기본값은 "USER"이며, 나중에 "ADMIN" 등으로 확장 가능합니다.
     */
    @Column(nullable = false, length = 20)  // JPA: NOT NULL, 최대 20자
    private String role = "USER";

    /**
     * 사용자 승인 상태
     * 회원가입 후 관리자 승인을 받아야 로그인이 가능합니다.
     * 기본값은 PENDING(승인대기)입니다.
     */
    @Enumerated(EnumType.STRING)  // JPA: Enum을 문자열로 저장
    @Column(nullable = false, length = 20)
    private UserStatus status = UserStatus.PENDING;

    /**
     * 빌더 패턴을 사용한 생성자
     * @Builder 어노테이션으로 빌더 클래스가 자동 생성됩니다.
     * 
     * 사용 예시:
     * User user = User.builder()
     *     .username("testuser")
     *     .password("encryptedPassword")
     *     .email("test@example.com")
     *     .name("홍길동")
     *     .role("USER")
     *     .build();
     */
    @Builder  // Lombok: 빌더 패턴 자동 생성
    public User(String userId, String password, String name, String position, String role, UserStatus status) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.position = position;
        this.role = role;
        this.status = status != null ? status : UserStatus.PENDING;
    }

    /**
     * 사용자 승인 처리
     * PENDING 상태에서 APPROVED 상태로 변경합니다.
     */
    public void approve() {
        this.status = UserStatus.APPROVED;
    }

    /**
     * 사용자 승인 거부 처리
     * PENDING 상태에서 REJECTED 상태로 변경합니다.
     */
    public void reject() {
        this.status = UserStatus.REJECTED;
    }

    /**
     * 사용자가 승인된 상태인지 확인
     * @return 승인된 상태이면 true
     */
    public boolean isApproved() {
        return this.status == UserStatus.APPROVED;
    }
}
