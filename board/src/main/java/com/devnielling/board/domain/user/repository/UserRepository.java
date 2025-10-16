package com.devnielling.board.domain.user.repository;

import com.devnielling.board.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    // 아이디 검증용 메서드
    Boolean existsByUsername(String username);

    // optional로 갑싸서 null 예외 값 방지
    Optional<UserEntity> findByUsername(String username);

    @Transactional
    void deleteByUsername(String username);
}
