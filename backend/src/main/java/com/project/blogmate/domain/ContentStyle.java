package com.project.blogmate.domain;

import java.util.Arrays;
import java.util.Optional;

public enum ContentStyle {
    PROFESSIONAL,
    CASUAL,
    INFORMATIVE,
    PERSUASIVE,
    ;

    public static ContentStyle of(String contentStyle) {
        return findByString(contentStyle)
                .orElse(CASUAL);
    }

    private static Optional<ContentStyle> findByString(String contentStyle) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(contentStyle))
                .findFirst();
    }
}
