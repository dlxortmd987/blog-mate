package com.project.blogmate.api;

import com.project.blogmate.domain.ContentLength;
import com.project.blogmate.domain.ContentRawData;
import com.project.blogmate.domain.ContentStyle;
import com.project.blogmate.domain.ContentType;

public record TransformRequest(
        String contentStyle,
        String contentLength,
        String contentType,
        String draft
) {
    public ContentRawData toRawData() {
        return new ContentRawData(
                ContentStyle.of(contentStyle),
                ContentLength.of(contentLength),
                ContentType.of(contentType),
                draft
        );
    }
}
