package com.david.CorpMemberLibrary.web;

import com.david.CorpMemberLibrary.domain.user.User;
import com.david.CorpMemberLibrary.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
     * 승인 대기 중인 사용자 목록 페이지
     *
     * @param model 뷰에 데이터를 전달하기 위한 Model 객체
     * @return 뷰 이름 (templates/admin/users-pending.html)
     */
    @GetMapping("/users/pending")
    public String pendingUsers(Model model) {
        List<User> pendingUsers = userService.findPendingUsers();
        model.addAttribute("pendingUsers", pendingUsers);
        model.addAttribute("pendingCount", pendingUsers.size());
        return "admin/users-pending";
    }

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

    /**
     * 사용자 승인 처리
     *
     * @param id 승인할 사용자의 ID
     * @param redirectAttributes 리다이렉트 시 전달할 메시지
     * @return 리다이렉트 경로
     */
    @PostMapping("/users/{id}/approve")
    public String approveUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.approveUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "사용자가 승인되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users/pending";
    }

    /**
     * 사용자 승인 거부 처리
     *
     * @param id 거부할 사용자의 ID
     * @param redirectAttributes 리다이렉트 시 전달할 메시지
     * @return 리다이렉트 경로
     */
    @PostMapping("/users/{id}/reject")
    public String rejectUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.rejectUser(id);
            redirectAttributes.addFlashAttribute("successMessage", "사용자가 거부되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/users/pending";
    }
}
