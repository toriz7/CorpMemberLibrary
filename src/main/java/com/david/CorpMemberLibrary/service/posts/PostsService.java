package com.david.CorpMemberLibrary.service.posts;

import com.david.CorpMemberLibrary.domain.posts.Posts;
import com.david.CorpMemberLibrary.domain.posts.PostsRepository;
import com.david.CorpMemberLibrary.web.dto.posts.PostsResponseDto;
import com.david.CorpMemberLibrary.web.dto.posts.PostsSaveRequestDto;
import com.david.CorpMemberLibrary.web.dto.posts.PostsUpdateRequestDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 게시글 비즈니스 로직을 처리하는 서비스 계층
 * 
 * Service 계층의 역할:
 * 1. 비즈니스 로직 처리
 * 2. 트랜잭션 관리
 * 3. Repository와 Controller 사이의 중간 계층
 * 
 * @Service: Spring이 이 클래스를 서비스 빈으로 등록
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성 (의존성 주입용)
 */
@Service  // Spring: 이 클래스를 서비스 빈으로 등록하여 의존성 주입 가능하게 함
@RequiredArgsConstructor  // Lombok: final 필드에 대한 생성자 자동 생성 (의존성 주입)
public class PostsService {
    
    /**
     * 게시글 데이터 접근 계층 (Repository)
     * 
     * @RequiredArgsConstructor로 자동 생성된 생성자를 통해
     * Spring이 PostsRepository를 주입해줍니다 (의존성 주입, DI)
     * 
     * final로 선언한 이유:
     * 1. 불변성 보장
    2. 생성자 주입 방식 사용 (권장되는 방식)
     */
    private final PostsRepository postsRepository;
    
    /**
     * 게시글 저장 메서드
     * 
     * @Transactional이 없는 이유:
     * JpaRepository의 save() 메서드가 이미 트랜잭션을 포함하고 있기 때문
     * 
     * @param requestDto 저장할 게시글 데이터
     * @return 저장된 게시글의 ID
     */
    public Long save(PostsSaveRequestDto requestDto) {
        // DTO를 Entity로 변환
        // Builder 패턴을 사용하여 Entity 생성
        Posts posts = Posts.builder()
                .title(requestDto.getTitle())  // DTO에서 제목 가져오기
                .content(requestDto.getContent())  // DTO에서 내용 가져오기
                .author(requestDto.getAuthor())  // DTO에서 작성자 가져오기
                .build();  // Posts 객체 생성
        
        // Repository를 통해 데이터베이스에 저장
        // save() 메서드는 저장된 Entity를 반환
        Posts savedPosts = postsRepository.save(posts);
        
        // 저장된 게시글의 ID를 반환
        return savedPosts.getId();
    }
    
    /**
     * 게시글 수정 메서드
     * 
     * @Transactional 어노테이션:
     * - 이 메서드가 실행되는 동안 하나의 트랜잭션으로 묶임
     * - 메서드 실행 중 예외 발생 시 모든 변경사항이 롤백됨
     * - 데이터 일관성 보장
     * 
     * @param requestDto 수정할 게시글 데이터 (ID 포함)
     * @return 수정된 게시글의 ID
     * @throws IllegalArgumentException 게시글이 존재하지 않을 때
     */
    @Transactional  // Spring: 이 메서드를 트랜잭션으로 묶음
    public Long update(PostsUpdateRequestDto requestDto) {
        // 수정할 게시글을 데이터베이스에서 조회
        // findById()는 Optional<Posts>를 반환하므로 orElseThrow()로 예외 처리
        Posts posts = postsRepository.findById(requestDto.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 게시글이 없습니다. id=" + requestDto.getId()));
        
        // Entity의 update 메서드를 호출하여 필드 수정
        // 도메인 모델에 비즈니스 로직을 포함하는 방식
        posts.update(
                requestDto.getTitle(),  // 새로운 제목으로 변경
                requestDto.getContent(),  // 새로운 내용으로 변경
                requestDto.getAuthor()  // 새로운 작성자로 변경
        );
        
        // JPA의 더티 체킹(Dirty Checking) 기능으로
        // 트랜잭션이 끝날 때 자동으로 UPDATE 쿼리가 실행됨
        // 별도로 save()를 호출하지 않아도 됨!
        // 하지만 명시적으로 save()를 호출해도 무방함
        
        // 수정된 게시글의 ID 반환
        return posts.getId();
    }
    
    /**
     * 게시글 ID로 조회 메서드
     * 
     * @param id 조회할 게시글 ID
     * @return 게시글 응답 DTO
     * @throws IllegalArgumentException 게시글이 존재하지 않을 때
     */
    public PostsResponseDto findById(Long id) {
        // 데이터베이스에서 게시글 조회
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 게시글이 없습니다. id=" + id));
        
        // Entity를 DTO로 변환하여 반환
        // Entity를 직접 반환하지 않고 DTO로 변환하는 이유:
        // 1. 보안: Entity의 모든 필드를 노출하지 않음
        // 2. 유연성: 필요한 데이터만 선택적으로 전달
        return new PostsResponseDto(posts);
    }
    
    /**
     * 전체 게시글 목록 조회 메서드
     * 
     * @return 게시글 응답 DTO 리스트
     */
    public List<PostsResponseDto> findAll() {
        // 데이터베이스에서 전체 게시글 조회
        List<Posts> postsList = postsRepository.findAll();
        
        // Stream API를 사용하여 Entity 리스트를 DTO 리스트로 변환
        // map(): 각 Posts Entity를 PostsResponseDto로 변환
        // collect(): 변환된 DTO들을 리스트로 수집
        return postsList.stream()
                .map(PostsResponseDto::new)  // 각 Entity를 DTO로 변환
                .collect(Collectors.toList());  // 리스트로 수집
    }
    
    /**
     * 게시글 삭제 메서드
     * 
     * @Transactional 어노테이션:
     * 삭제 작업도 트랜잭션으로 묶어서 안전하게 처리
     * 
     * @param id 삭제할 게시글 ID
     * @throws IllegalArgumentException 게시글이 존재하지 않을 때
     */
    @Transactional  // Spring: 이 메서드를 트랜잭션으로 묶음
    public void delete(Long id) {
        // 삭제할 게시글을 먼저 조회
        // 존재하지 않으면 예외 발생
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "해당 게시글이 없습니다. id=" + id));
        
        // Repository를 통해 데이터베이스에서 삭제
        postsRepository.delete(posts);
    }
}

