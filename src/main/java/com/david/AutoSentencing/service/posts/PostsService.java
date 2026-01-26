package com.david.AutoSentencing.service.posts;

import com.david.AutoSentencing.domain.posts.Posts;
import com.david.AutoSentencing.domain.posts.PostsRepository;
import com.david.AutoSentencing.dto.posts.PostsResponseDto;
import com.david.AutoSentencing.dto.posts.PostsSaveRequestDto;
import com.david.AutoSentencing.dto.posts.PostsUpdateRequestDto;
import com.david.AutoSentencing.exception.PostNotFoundException;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostsService {

    private final PostsRepository postsRepository;
    
    @Transactional
    public void save(PostsSaveRequestDto requestDto) {
        log.info("게시글 저장 요청 - 제목: {}, 작성자: {}", requestDto.getTitle(), requestDto.getAuthor());

        Posts savedPosts = postsRepository.save(requestDto.toEntity());

        log.info("게시글 저장 완료 - ID: {}", savedPosts.getId());
    }
    
    @Transactional
    public Long update(PostsUpdateRequestDto requestDto) {
        log.info("게시글 수정 요청 - ID: {}", requestDto.getId());

        Posts posts = postsRepository.findById(requestDto.getId())
                .orElseThrow(() -> new PostNotFoundException(requestDto.getId()));

        posts.update(
                requestDto.getTitle(),
                requestDto.getContent(),
                requestDto.getAuthor()
        );

        log.info("게시글 수정 완료 - ID: {}", posts.getId());
        return posts.getId();
    }
    
    public PostsResponseDto findById(Long id) {
        log.info("게시글 조회 요청 - ID: {}", id);

        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        return new PostsResponseDto(posts);
    }
    
    public List<PostsResponseDto> findAll() {
        log.info("전체 게시글 목록 조회 요청");

        List<Posts> postsList = postsRepository.findAll();

        log.info("조회된 게시글 수: {}", postsList.size());
        return postsList.stream()
                .map(PostsResponseDto::new)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public void delete(Long id) {
        log.info("게시글 삭제 요청 - ID: {}", id);

        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id));

        postsRepository.delete(posts);
        log.info("게시글 삭제 완료 - ID: {}", id);
    }
}

