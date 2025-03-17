package com.project.blogmate.infra.openai;

import java.util.List;

public record OpenAiTransformResponse(
        String content,
        List<String> titles,
        List<String> tags
) {
}
