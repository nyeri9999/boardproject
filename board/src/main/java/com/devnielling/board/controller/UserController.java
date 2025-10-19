package com.devnielling.board.controller;

import com.devnielling.board.domain.user.dto.UserRequestDTO;
import com.devnielling.board.domain.user.dto.UserResponseDTO;
import com.devnielling.board.domain.user.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 회원가입
    // 회원가입 페이지 응답
    @GetMapping("/user/join")
    public String JoinPage() {
        return "join";
    }
    // 회원가입 수행
    @PostMapping
    public String joinProcess(UserRequestDTO dto) {
        userService.createOneUser(dto);
        return "redirect:/user/list";
    }

    // 회원수정
    @GetMapping
    public String updatePage(@PathVariable("username") String username, Model model) {

        if (userService.isAccess(username)) {
            UserResponseDTO dto = userService.readOneUser(username);
            model.addAttribute("USER", dto);
            return "update";
        }
        return "redirect:/user/update/" + username;
    }

    @PostMapping("/user/update/{username}")
    public String updateProcess(@PathVariable("username") String username, UserRequestDTO dto) {

        // User(본인) or admin 권한만 접근 가능
        if (userService.isAccess(username)) {
            userService.updateOneUser(dto, username);
        }
        return "redirect:/user/update" + username;
    }
}
