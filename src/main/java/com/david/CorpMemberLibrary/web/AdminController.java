package com.david.CorpMemberLibrary.web;

import com.david.CorpMemberLibrary.domain.user.User;
import com.david.CorpMemberLibrary.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 관리자 기능 관련 웹 요청을 처리하는 컨트롤러
 * /admin/** 경로는 ADMIN 권한을 가진 사용자만 접근 가능합니다.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    /**
     * 전체 사용자 목록 페이지
     *
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 뷰 이름 (templates/admin/users-list.html)
     */
    @GetMapping("/users")
    public String allUsers(Model model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "admin/users-list";
    }
}
