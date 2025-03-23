package com.project.blogmate.domain;

public record ContentRawData(
        ContentStyle contentStyle,
        ContentLength contentLength,
        ContentType contentType,
        String draft
) {
}
