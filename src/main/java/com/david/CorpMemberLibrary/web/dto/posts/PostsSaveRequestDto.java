package com.david.CorpMemberLibrary.web.dto.posts;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 게시글 저장 요청 DTO (Data Transfer Object)
 * 
 * 클라이언트에서 게시글을 생성할 때 전달받는 데이터를 담는 객체입니다.
 * DTO를 사용하는 이유:
 * 1. Entity를 직접 노출하지 않아 보안 강화
 * 2. 필요한 필드만 받아서 유효성 검증 용이
 * 3. API 스펙 변경 시 Entity 수정 없이 DTO만 수정 가능
 */
@Getter  // Lombok: 모든 필드에 대한 getter 메서드 자동 생성
@NoArgsConstructor  // Lombok: 기본 생성자 자동 생성
public class PostsSaveRequestDto {
    
    /**
     * 게시글 제목
     * 클라이언트로부터 받아서 저장할 제목 데이터
     */
    private String title;
    
    /**
     * 게시글 내용
     * 클라이언트로부터 받아서 저장할 내용 데이터
     */
    private String content;
    
    /**
     * 게시글 작성자
     * 클라이언트로부터 받아서 저장할 작성자 데이터
     */
    private String author;
    
    /**
     * 모든 필드를 받는 생성자
     * 
     * @param title 게시글 제목
     * @param content 게시글 내용
     * @param author 게시글 작성자
     */
    public PostsSaveRequestDto(String title, String content, String author) {
        // 생성자를 통해 필드 초기화
        this.title = title;
        this.content = content;
        this.author = author;
    }
}

