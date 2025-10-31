package com.devnielling.board.domain.board.service;

import com.devnielling.board.domain.board.dto.BoardRequestDTO;
import com.devnielling.board.domain.board.dto.BoardResponseDTO;
import com.devnielling.board.domain.board.entity.BoardEntity;
import com.devnielling.board.domain.board.repository.BoardRepository;
import com.devnielling.board.domain.user.entity.UserEntity;
import com.devnielling.board.domain.user.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    public BoardService(BoardRepository boardRepository, UserRepository userRepository) {
        this.boardRepository = boardRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void createOneBoard(BoardRequestDTO dto) {

        // dto -> entity
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setTitle(dto.getTitle());
        boardEntity.setContent(dto.getContent());

        // 엔티티 저장
        boardRepository.save(boardEntity);

        // user와 board 연관관계 생성

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow();

        userEntity.addBoardEntity(boardEntity);
        userRepository.save(userEntity);
    }

    @Transactional(readOnly = true)
    public BoardResponseDTO readOneBoard(Long id) {
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow();

        BoardResponseDTO dto = new BoardResponseDTO();
        dto.setId(boardEntity.getId());
        dto.setTitle(boardEntity.getTitle());
        dto.setContent(boardEntity.getContent());

        return dto;
    }

    // 게시글 모두 읽기
    @Transactional(readOnly = true)
    public List<BoardResponseDTO> readAllBoard() {

        List<BoardEntity> list = boardRepository.findAll();

        List<BoardResponseDTO> dtos = new ArrayList<>();
        for (BoardEntity boardEntity : list) {
            BoardResponseDTO dto = new BoardResponseDTO();
            dto.setId(boardEntity.getId());
            dto.setTitle(boardEntity.getTitle());
            dto.setContent(boardEntity.getContent());

            dtos.add(dto);
        }

        return dtos;
    }

    // 게시글 하나 수정
    @Transactional(readOnly = true)
    public void updateOneBoard(Long id, BoardRequestDTO dto) {
        BoardEntity boardEntity = boardRepository.findById(id).orElseThrow();

        boardEntity.setTitle(dto.getTitle());
        boardEntity.setContent(dto.getContent());
        boardRepository.save(boardEntity);
    }

    // 특정 게시물 하나 삭제
    @Transactional
    public void deleteOneBoard(Long id) {
        boardRepository.deleteById(id);
    }

    // 유저 접근 권한 체크
    public Boolean isAccess(Long id) {

        String sessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String sessionRole =  SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();

        if ("ROLE_ADMIN".equals(sessionRole)) {
            return true;
        }

        String boardUsername = boardRepository.findById(id).orElseThrow().getUserEntity().getUsername();
        if (sessionUsername.equals(boardUsername)) {
            return true;
        }

        return false;
    }
}
