package com.project.blogmate.domain;

public record ContentRawData(
        ContentLength contentLength,
        ContentType contentType,
        String draft
) {
}
