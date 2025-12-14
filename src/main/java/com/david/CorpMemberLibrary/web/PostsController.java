package com.david.CorpMemberLibrary.web;

import com.david.CorpMemberLibrary.service.posts.PostsService;
import com.david.CorpMemberLibrary.web.dto.posts.PostsResponseDto;
import com.david.CorpMemberLibrary.web.dto.posts.PostsSaveRequestDto;
import com.david.CorpMemberLibrary.web.dto.posts.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시글 웹 요청을 처리하는 컨트롤러 계층
 * 
 * Controller 계층의 역할:
 * 1. 클라이언트의 HTTP 요청을 받아서 처리
 * 2. 요청 데이터를 DTO로 변환
 * 3. Service 계층에 비즈니스 로직 처리를 위임
 * 4. 처리 결과를 클라이언트에 응답
 * 
 * @Controller: Spring MVC에서 뷰를 반환하는 컨트롤러로 등록
 * @RequiredArgsConstructor: final 필드에 대한 생성자 자동 생성 (의존성 주입용)
 */
@Controller  // Spring: 이 클래스를 컨트롤러 빈으로 등록
@RequiredArgsConstructor  // Lombok: final 필드에 대한 생성자 자동 생성
public class PostsController {
    
    /**
     * 게시글 비즈니스 로직 서비스
     * 
     * @RequiredArgsConstructor로 자동 생성된 생성자를 통해
     * Spring이 PostsService를 주입해줍니다 (의존성 주입, DI)
     */
    private final PostsService postsService;
    
    /**
     * 게시글 작성 폼 페이지
     * 
     * @GetMapping: HTTP GET 요청을 처리
     * "/posts/save" 경로로 GET 요청이 오면 이 메서드가 실행됨
     * 
     * @return 뷰 이름 (templates/posts/posts-save.html)
     */
    @GetMapping("/posts/save")  // GET /posts/save 요청 처리
    public String postsSave() {
        // 게시글 작성 폼 페이지로 이동
        return "posts/posts-save";
    }
    
    /**
     * 게시글 목록 페이지
     * 
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     *              Spring이 자동으로 주입해줌
     * @return 뷰 이름 (templates/posts/posts-list.html)
     */
    @GetMapping("/posts")  // GET /posts 요청 처리
    public String postsList(Model model) {
        // Service를 통해 전체 게시글 목록 조회
        List<PostsResponseDto> postsList = postsService.findAll();
        
        // Model에 게시글 목록을 추가
        // 뷰에서 "postsList"라는 이름으로 접근 가능
        model.addAttribute("postsList", postsList);
        
        // 게시글 목록 페이지로 이동
        return "posts/posts-list";
    }
    
    /**
     * 게시글 상세 조회 페이지
     * 
     * @PathVariable: URL 경로에서 변수 값을 추출
     * 예: /posts/1 -> id = 1
     * 
     * @param id 조회할 게시글 ID
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 뷰 이름 (templates/posts/posts-detail.html)
     */
    @GetMapping("/posts/{id}")  // GET /posts/{id} 요청 처리
    public String postsDetail(@PathVariable Long id, Model model) {
        // Service를 통해 게시글 조회
        PostsResponseDto dto = postsService.findById(id);
        
        // Model에 게시글 데이터를 추가
        // 뷰에서 "post"라는 이름으로 접근 가능
        model.addAttribute("post", dto);
        
        // 게시글 상세 페이지로 이동
        return "posts/posts-detail";
    }
    
    /**
     * 게시글 수정 폼 페이지
     * 
     * @param id 수정할 게시글 ID
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 뷰 이름 (templates/posts/posts-update.html)
     */
    @GetMapping("/posts/update/{id}")  // GET /posts/update/{id} 요청 처리
    public String postsUpdate(@PathVariable Long id, Model model) {
        // Service를 통해 수정할 게시글 조회
        PostsResponseDto dto = postsService.findById(id);
        
        // Model에 게시글 데이터를 추가
        model.addAttribute("post", dto);
        
        // 게시글 수정 폼 페이지로 이동
        return "posts/posts-update";
    }
    
    /**
     * 게시글 저장 처리 (POST 요청)
     * 
     * @PostMapping: HTTP POST 요청을 처리
     * @ModelAttribute: HTML 폼에서 전송된 데이터를 DTO로 자동 변환
     * 
     * @param requestDto 저장할 게시글 데이터
     * @return 리다이렉트 경로 (저장 후 목록 페이지로 이동)
     */
    @PostMapping("/posts/save")  // POST /posts/save 요청 처리
    public String save(PostsSaveRequestDto requestDto) {
        // Service를 통해 게시글 저장
        // 저장된 게시글의 ID를 반환받지만, 여기서는 사용하지 않음
        postsService.save(requestDto);
        
        // 저장 후 게시글 목록 페이지로 리다이렉트
        // "redirect:"를 붙이면 해당 경로로 리다이렉트됨
        return "redirect:/posts";
    }
    
    /**
     * 게시글 수정 처리 (POST 요청)
     * 
     * @param requestDto 수정할 게시글 데이터
     * @return 리다이렉트 경로 (수정 후 상세 페이지로 이동)
     */
    @PostMapping("/posts/update/{id}")  // POST /posts/update/{id} 요청 처리
    public String update(@PathVariable Long id, PostsUpdateRequestDto requestDto) {
        // URL에서 받은 ID를 DTO에 설정
        requestDto = new PostsUpdateRequestDto(
                id,  // URL에서 받은 ID
                requestDto.getTitle(),  // 폼에서 받은 제목
                requestDto.getContent(),  // 폼에서 받은 내용
                requestDto.getAuthor()  // 폼에서 받은 작성자
        );
        
        // Service를 통해 게시글 수정
        Long updatedId = postsService.update(requestDto);
        
        // 수정 후 해당 게시글의 상세 페이지로 리다이렉트
        return "redirect:/posts/" + updatedId;
    }
    
    /**
     * 게시글 삭제 처리 (POST 요청)
     * 
     * @param id 삭제할 게시글 ID
     * @return 리다이렉트 경로 (삭제 후 목록 페이지로 이동)
     */
    @PostMapping("/posts/delete/{id}")  // POST /posts/delete/{id} 요청 처리
    public String delete(@PathVariable Long id) {
        // Service를 통해 게시글 삭제
        postsService.delete(id);
        
        // 삭제 후 게시글 목록 페이지로 리다이렉트
        return "redirect:/posts";
    }
}

