package com.project.blogmate.domain;

import java.util.Arrays;
import java.util.Optional;

public enum ContentLength {
    MIN(500, 1000),
    MEDIUM(1000, 2000),
    MAX(2000, 3000),
    ;

    int lowerBound;
    int upperBound;

    ContentLength(int lowerBound, int upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public static ContentLength of(String contentLength) {
        return findByString(contentLength)
                .orElse(MEDIUM);
    }

    private static Optional<ContentLength> findByString(String contentLength) {
        return Arrays.stream(values())
                .filter(it -> it.name().equals(contentLength))
                .findFirst();
    }

    public int getLowerBound() {
        return lowerBound;
    }

    public int getUpperBound() {
        return upperBound;
    }
}
