package com.david.CorpMemberLibrary.web.dto.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 수정 요청 DTO
 * 
 * 클라이언트에서 게시글을 수정할 때 전달받는 데이터를 담는 객체입니다.
 * 수정 요청 DTO를 별도로 만드는 이유:
 * 1. 저장과 수정 시 필요한 필드가 다를 수 있음
 * 2. 수정 시 ID가 필요하지만 저장 시에는 불필요
 * 3. 각각의 용도에 맞는 유효성 검증 가능
 */
@Getter  // Lombok: 모든 필드에 대한 getter 메서드 자동 생성
@NoArgsConstructor  // Lombok: 기본 생성자 자동 생성
public class PostsUpdateRequestDto {
    
    /**
     * 수정할 게시글의 ID
     * 어떤 게시글을 수정할지 식별하기 위한 필수 필드
     */
    private Long id;
    
    /**
     * 수정할 게시글 제목
     */
    private String title;
    
    /**
     * 수정할 게시글 내용
     */
    private String content;
    
    /**
     * 수정할 게시글 작성자
     */
    private String author;
    
    /**
     * 모든 필드를 받는 생성자
     * 
     * @param id 게시글 ID
     * @param title 수정할 제목
     * @param content 수정할 내용
     * @param author 수정할 작성자
     */
    public PostsUpdateRequestDto(Long id, String title, String content, String author) {
        // 생성자를 통해 필드 초기화
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
    }
}

