package com.devnielling.board.controller;

import com.devnielling.board.domain.board.dto.BoardRequestDTO;
import com.devnielling.board.domain.board.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    // 글 생성시 페이지 응답
    @GetMapping("/board/create")
    public String createPage() {
        return "board/create";
    }
    // 글 생성 후 수정 시
    @PostMapping("/board/create")
    public String createProcess(BoardRequestDTO dto) {
        boardService.createOneBoard(dto);
        return "redirect:/board/read";
    }
    // 글 목록 페이지 응답
    @GetMapping("/board/read")
    public String readPage(Model model) {
        model.addAttribute("BOARDLIST", boardService.readAllBoard());
        return "readBoard";
    }
    // 글 읽어오기: 페이지 응답
    @GetMapping("/board/read/{id}")
    public String readIdPage(Model model, @PathVariable("id") Long id) {
        model.addAttribute("BOARD", boardService.readOneBoard(id));

        return "readIdBoard";
    }

    @GetMapping("/board/update/{id}")
    public String updatePage(Model model, @PathVariable("id") Long id) {

        if (!boardService.isAccess(id)) {
            return "redirect:/board/read";
        }
        model.addAttribute("BOARD", boardService.readOneBoard(id));
        return "updateBoard";
    }

    @PostMapping("/board/update/{id}")
    public String updateProcess(BoardRequestDTO dto, @PathVariable("id") Long id) {

        if (!boardService.isAccess(id)) {
            return "redirect:board/read";
        }
        boardService.updateOneBoard(id, dto);

        return "redirect:/board/read" + id;

    }

    // 글 삭제:
    @PostMapping("board/delete/{id}")
    public String deleteProcess(@PathVariable("id") Long id) {

        if (boardService.isAccess(id)) {
            boardService.deleteOneBoard(id);
        }

        return "redirect:/board/read";
    }
}
