package com.david.CorpMemberLibrary.domain.posts;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 엔티티 클래스
 * JPA를 사용하여 데이터베이스의 posts 테이블과 매핑됩니다.
 */
@Getter  // Lombok: 모든 필드에 대한 getter 메서드 자동 생성
@NoArgsConstructor  // Lombok: 기본 생성자 자동 생성 (JPA 필수)
@Entity  // JPA: 이 클래스를 엔티티로 지정
@Table(name = "posts")  // JPA: 데이터베이스 테이블명을 "posts"로 지정
public class Posts {

    /**
     * 게시글 고유 ID (기본키)
     * 데이터베이스에서 자동으로 증가하는 값으로 생성됩니다.
     */
    @Id  // JPA: 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // JPA: ID 자동 생성 전략 (AUTO_INCREMENT)
    private Long id;


    /**
     * 게시글 제목
     * 최대 500자까지 저장 가능하며, 필수 입력 항목입니다.
     */
    @Column(length= 500, nullable = false)  // JPA: 컬럼 길이 500, NOT NULL 제약조건
    private String title;

    /**
     * 게시글 내용
     * TEXT 타입으로 저장되며, 필수 입력 항목입니다.
     */
    @Column(columnDefinition = "TEXT", nullable = false)  // JPA: TEXT 타입, NOT NULL 제약조건
    private String content;

    /**
     * 게시글 작성자
     * 선택 입력 항목입니다.
     */
    private String author;

    /**
     * 빌더 패턴을 사용한 생성자
     * @Builder 어노테이션으로 빌더 클래스가 자동 생성됩니다.
     * 
     * 사용 예시:
     * Posts post = Posts.builder()
     *     .title("제목")
     *     .content("내용")
     *     .author("작성자")
     *     .build();
     */
    @Builder  // Lombok: 빌더 패턴 자동 생성
    public Posts(String title, String content,String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
    
    /**
     * 게시글 정보 수정 메서드
     * 
     * 도메인 모델에 비즈니스 로직을 포함하는 방식입니다.
     * 이렇게 하는 이유:
     * 1. Entity가 자신의 상태를 관리 (응집도 향상)
     * 2. Service 계층의 코드가 간결해짐
     * 3. 도메인 주도 설계(DDD) 원칙에 부합
     * 
     * @param title 수정할 제목
     * @param content 수정할 내용
     * @param author 수정할 작성자
     */
    public void update(String title, String content, String author) {
        // Entity의 필드를 직접 수정
        // JPA의 더티 체킹(Dirty Checking)으로 자동으로 UPDATE 쿼리 실행
        this.title = title;
        this.content = content;
        this.author = author;
    }
}
