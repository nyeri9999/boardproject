package com.devnielling.board.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDTO {

    // 유저 아이디, 패스워드와 권한 정보가 response로
    private String username;
    private String nickname;
    private String Role;

}
