package com.david.CorpMemberLibrary.web.dto.posts;

import com.david.CorpMemberLibrary.domain.posts.Posts;
import lombok.Getter;

/**
 * 게시글 응답 DTO
 * 
 * 서버에서 클라이언트로 게시글 데이터를 전달할 때 사용하는 객체입니다.
 * Entity를 직접 반환하지 않고 DTO를 사용하는 이유:
 * 1. Entity의 모든 필드를 노출하지 않아 보안 강화
 * 2. 필요한 데이터만 선택적으로 전달 가능
 * 3. 순환 참조 문제 방지 (Entity 간 관계가 있을 때)
 * 4. API 응답 형식 통일
 */
@Getter  // Lombok: 모든 필드에 대한 getter 메서드 자동 생성
public class PostsResponseDto {
    
    /**
     * 게시글 고유 ID
     */
    private Long id;
    
    /**
     * 게시글 제목
     */
    private String title;
    
    /**
     * 게시글 내용
     */
    private String content;
    
    /**
     * 게시글 작성자
     */
    private String author;
    
    /**
     * Entity를 DTO로 변환하는 생성자
     * 
     * 이 생성자는 Entity 객체를 받아서 DTO 객체로 변환합니다.
     * 이렇게 하는 이유:
     * 1. Entity와 DTO의 책임 분리
     * 2. Entity는 DB 매핑, DTO는 데이터 전송에만 집중
     * 
     * @param entity 변환할 Posts 엔티티 객체
     */
    public PostsResponseDto(Posts entity) {
        // Entity의 필드 값을 DTO 필드에 복사
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}

