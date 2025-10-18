package com.devnielling.board.domain.user.service;

import com.devnielling.board.domain.user.dto.UserRequestDTO;
import com.devnielling.board.domain.user.dto.UserResponseDTO;
import com.devnielling.board.domain.user.entity.UserEntity;
import com.devnielling.board.domain.user.entity.UserRoleType;
import com.devnielling.board.domain.user.repository.UserRepository;
import lombok.Builder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Builder
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createOneUser(UserRequestDTO dto) {

        // 유저정보를 dto로부터 getter로 가져와서 담음.
        String username = dto.getUsername();
        String password = dto.getPassword();
        String nickname = dto.getNickname();

        if (userRepository.existsByUsername(username)) {
            return; // 문제 없으면 패스
        }

        UserEntity entity = new UserEntity();

        entity.setUsername(username);
        entity.setPassword(passwordEncoder.encode(password));
        entity.setNickname(nickname);
        entity.setRole(UserRoleType.USER);

        userRepository.save(entity);
    }

    // 특정 유저 읽어오기.
    @Transactional(readOnly = true)
    public UserResponseDTO readOneUser(String username) {

        UserEntity entity = userRepository.findByUsername(username).orElseThrow();

        UserResponseDTO dto = new UserResponseDTO();
        dto.setUsername(entity.getUsername());
        dto.setNickname(entity.getNickname());
        dto.setRole(entity.getRole().toString());

        return dto;
    }

    @Transactional(readOnly = true)
    public List<UserResponseDTO> readAllUsers() {

        List<UserEntity> list = userRepository.findAll();
        List<UserResponseDTO> dtos = new ArrayList<>();
        for (UserEntity user : list) {
            UserResponseDTO dto = new UserResponseDTO();
            dto.setUsername(user.getUsername());
            dto.setNickname(user.getNickname());
            dto.setRole(user.getRole().toString());

            dtos.add(dto);
        }

        return dtos;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity entity = userRepository.findByUsername(username).orElseThrow();

        return User.builder()
                .username(entity.getUsername())
                .password(entity.getPassword())
                .roles(entity.getRole().toString())
                .build();
    }

    @Transactional
    public void updateOneUser(UserRequestDTO dto, String username) {

        UserEntity entity = userRepository.findByUsername(username).orElseThrow();

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if(dto.getNickname() != null && !dto.getNickname().isEmpty()) {
            entity.setNickname(dto.getNickname());
        }

        userRepository.save(entity);
    }

    @Transactional
    public void deleteOneUser(String username) {
        userRepository.deleteByUsername(username);
    }

    public Boolean isAccess(String username) {
        String sessionUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        String sessionRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities().iterator().next().getAuthority();

        if ("ROLE_ADMIN".equals(sessionRole)) {
            return true;
        }

        if (username.equals(sessionUsername)) {
            return true;
        }

        return false;
    }
}

