package com.devnielling.board.domain.user.entity;

import com.devnielling.board.domain.board.entity.BoardEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private UserRoleType role;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardEntity> boardEntityList = new ArrayList<>();

    // 유저에 대한 새로운 글 추가 시, 추가할 글을 받아 연관관계에 매핑해준다.
    public void addBoardEntity(BoardEntity entity) {
        entity.setUserEntity(this);
        boardEntityList.add(entity);
    }

    // 유저에 대하여 기존 글 삭제 시, 삭제할 글을 받아 연관관계에서 뺀다.
    public void removeBoardEntity(BoardEntity entity) {
        entity.setUserEntity(null);
        boardEntityList.remove(entity);
    }
}
