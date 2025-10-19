package com.devnielling.board.domain.board.repository;

import com.devnielling.board.domain.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardEntity, Long> {

    //

}
