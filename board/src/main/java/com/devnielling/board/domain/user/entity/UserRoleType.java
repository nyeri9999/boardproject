package com.devnielling.board.domain.user.entity;

import lombok.Getter;

@Getter
public enum UserRoleType {
    ADMIN("ADMIN"),
    USER("USER");

    private final String description;

    UserRoleType(String description) {
        this.description = description;
    }
}
