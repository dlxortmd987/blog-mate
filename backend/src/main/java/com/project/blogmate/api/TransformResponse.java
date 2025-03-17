package com.project.blogmate.api;

import com.project.blogmate.domain.TransformResult;

import java.util.List;

public record TransformResponse(
        List<String> titles,
        String content,
        List<String> keywords
) {
    public static TransformResponse from(TransformResult result) {
        return new TransformResponse(
                result.titles(),
                result.content(),
                result.keywords()
        );
    }
}
