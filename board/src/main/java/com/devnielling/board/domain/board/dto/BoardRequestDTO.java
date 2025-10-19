package com.devnielling.board.domain.board.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardRequestDTO {

    public String title; // 글 제목
    public String content; // 글 내용
}
