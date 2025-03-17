package com.project.blogmate.infra.openai;

import com.project.blogmate.domain.ContentRawData;
import com.project.blogmate.domain.TransformResult;
import com.project.blogmate.domain.TransformService;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class OpenAiBlogTransformService implements TransformService {
    private final OpenAiChatModel chatModel;

    public OpenAiBlogTransformService(OpenAiChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public TransformResult transform(ContentRawData contentRawData) {
        BeanOutputConverter<TransformResult> converter = new BeanOutputConverter<>(TransformResult.class);

        String content = chatModel.call(PromptBuilder.buildTransformPrompt(
                        contentRawData,
                        converter.getJsonSchema()
                )).getResult()
                .getOutput()
                .getText();

        return converter.convert(content);
    }
}
