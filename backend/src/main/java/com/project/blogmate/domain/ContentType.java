package com.project.blogmate.domain;

import java.util.Arrays;
import java.util.Optional;

public enum ContentType {
    FOOD,
    PROGRAMMING,
    TRAVEL,
    LIFESTYLE,
    NONE,
    ;

    public static ContentType of(String contentType) {
        return findByString(contentType)
                .orElse(NONE);
    }

    private static Optional<ContentType> findByString(String contentType) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(contentType))
                .findFirst();
    }
}
