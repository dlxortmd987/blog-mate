package com.project.blogmate.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record TransformResult(
        @JsonProperty(required = true, value = "content")
        String content,
        @JsonProperty(required = true, value = "titles")
        List<String> titles,
        @JsonProperty(required = true, value = "keywords")
        List<String> keywords
) {
}
