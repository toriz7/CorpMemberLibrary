package com.david.CorpMemberLibrary.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 게시글 데이터 접근 계층 (Repository)
 * Spring Data JPA를 사용하여 Posts 엔티티에 대한 데이터베이스 작업을 처리합니다.
 * 
 * JpaRepository를 상속받아 기본적인 CRUD 메서드가 자동으로 제공됩니다:
 * - save(Posts entity): 게시글 저장/수정
 * - findById(Long id): ID로 게시글 조회
 * - findAll(): 전체 게시글 조회
 * - delete(Posts entity): 게시글 삭제
 * - count(): 게시글 개수 조회
 * 등
 * 
 * 사용 예시:
 * @Autowired
 * private PostsRepository postsRepository;
 * 
 * Posts post = Posts.builder()
 *     .title("제목")
 *     .content("내용")
 *     .author("작성자")
 *     .build();
 * postsRepository.save(post);
 */
public interface PostsRepository extends JpaRepository<Posts,Long>  {
    // JpaRepository<Posts, Long>
    // - Posts: 엔티티 타입
    // - Long: 엔티티의 ID 타입

}
