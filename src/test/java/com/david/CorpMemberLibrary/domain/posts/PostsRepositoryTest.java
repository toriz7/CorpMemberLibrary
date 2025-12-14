package com.david.CorpMemberLibrary.domain.posts;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@DisplayName("PostsRepository 테스트")
class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    @AfterEach
    void tearDown() {
        postsRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글을 저장하고 조회할 수 있다")
    void testSaveAndFindById() {
        //given
        String title = "테스트 제목";
        String content = "테스트 내용";
        String author = "테스트 작성자";

        Posts post = Posts.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();

        //when
        Posts savedPost = postsRepository.save(post);
        Optional<Posts> foundPost = postsRepository.findById(savedPost.getId());

        //then
        assertThat(foundPost).isPresent();
        assertThat(foundPost.get().getTitle()).isEqualTo(title);
        assertThat(foundPost.get().getContent()).isEqualTo(content);
        assertThat(foundPost.get().getAuthor()).isEqualTo(author);
    }

    @Test
    @DisplayName("전체 게시글을 조회할 수 있다")
    void testFindAll() {
        //given
        Posts post1 = Posts.builder()
                .title("제목1")
                .content("내용1")
                .author("작성자1")
                .build();

        Posts post2 = Posts.builder()
                .title("제목2")
                .content("내용2")
                .author("작성자2")
                .build();

        postsRepository.save(post1);
        postsRepository.save(post2);

        //when
        List<Posts> posts = postsRepository.findAll();

        //then
        assertThat(posts).hasSize(2);
    }

    @Test
    @DisplayName("같은 게시글을 두 번 저장할 수 있다")
    void testSaveTwice() {
        //given
        Posts post = Posts.builder()
                .title("제목")
                .content("내용")
                .author("작성자")
                .build();

        //when
        Posts savedPost1 = postsRepository.save(post);
        Posts savedPost2 = postsRepository.save(post);

        //then
        assertThat(savedPost1).isNotNull();
        assertThat(savedPost2).isNotNull();
        assertThat(postsRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글을 삭제할 수 있다")
    void testDelete() {
        //given
        Posts post = Posts.builder()
                .title("삭제할 제목")
                .content("삭제할 내용")
                .author("삭제할 작성자")
                .build();

        Posts savedPost = postsRepository.save(post);
        Long postId = savedPost.getId();

        //when
        postsRepository.delete(savedPost);

        //then
        Optional<Posts> foundPost = postsRepository.findById(postId);
        assertThat(foundPost).isEmpty();
        assertThat(postsRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("게시글 개수를 조회할 수 있다")
    void testCount() {
        //given
        Posts post1 = Posts.builder()
                .title("제목1")
                .content("내용1")
                .author("작성자1")
                .build();

        Posts post2 = Posts.builder()
                .title("제목2")
                .content("내용2")
                .author("작성자2")
                .build();

        postsRepository.save(post1);
        postsRepository.save(post2);

        //when
        long count = postsRepository.count();

        //then
        assertThat(count).isEqualTo(2);
    }

    @Test
    @DisplayName("존재하지 않는 게시글을 조회하면 Optional.empty()를 반환한다")
    void testFindByIdNotFound() {
        //when
        Optional<Posts> foundPost = postsRepository.findById(999L);

        //then
        assertThat(foundPost).isEmpty();
    }
}

