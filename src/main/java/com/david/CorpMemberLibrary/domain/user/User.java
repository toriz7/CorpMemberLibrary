package com.david.CorpMemberLibrary.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 사용자 엔티티 클래스
 * JPA를 사용하여 데이터베이스의 UserTbl 테이블과 매핑됩니다.
 */
@Getter
@NoArgsConstructor
@Entity
@Table(name = "UserTbl")
public class User {

    /**
     * 사용자 ID (기본키)
     * 사번으로 사용됩니다.
     */
    @Id
    @Column(name = "UserId", length = 7, nullable = false)
    private String userId;

    /**
     * 사용자 비밀번호
     * BCrypt로 암호화되어 저장됩니다.
     */
    @Column(name = "UserPw", nullable = false)
    private String password;

    /**
     * 사용자 권한
     * USER, ADMIN 등
     */
    @Column(name = "UserPermission", length = 30, nullable = false)
    private String role = "USER";

    /**
     * 사용자 실명
     */
    @Column(name = "UserName", length = 30, nullable = false)
    private String name;

    /**
     * 사용자 직책
     */
    @Column(name = "UserPosition", length = 20, nullable = false)
    private String position;

    /**
     * 사용자 부서
     */
    @Column(name = "UserDept", length = 20)
    private String dept;

    /**
     * 생성일시
     */
    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @Builder
    public User(String userId, String password, String name, String position, String role, String dept) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.position = position;
        this.role = role != null ? role : "USER";
        this.dept = dept;
    }
}
