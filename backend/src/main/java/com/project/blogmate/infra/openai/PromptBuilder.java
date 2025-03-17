package com.project.blogmate.infra.openai;

import com.project.blogmate.domain.ContentRawData;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.openai.api.ResponseFormat;

public class PromptBuilder {

    public static Prompt buildTransformPrompt(ContentRawData contentRawData, String jsonSchema) {
        String prompt = """
                I want to write a blog post.
                I will provide a draft, and based on that draft, please generate a well-structured blog post.
                
                
                There are two options for customization:
                	1.	%d <= Blog Content Length <= %d
                	2.	Blog Content Type: %s
                
                Draft: %s
                
                Requirements:
                	•	The response should be written in Korean.
                	•	Suggest relevant titles that match the content of the blog post.
                	•	Recommend important keywords that should be included in the blog.
                	•	It must be a complete JSON format.
                	•	Additionally, the length refers to the number of non-whitespace characters within the content field.
                
                Ensure that the blog post is engaging, well-structured, and follows best writing practices.
                """.formatted(
                contentRawData.contentLength().getLowerBound(),
                contentRawData.contentLength().getUpperBound(),
                contentRawData.contentType(),
                contentRawData.draft()
        );
        ResponseFormat responseFormat = ResponseFormat.builder()
                .type(ResponseFormat.Type.JSON_SCHEMA)
                .jsonSchema(jsonSchema)
                .build();
        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .responseFormat(responseFormat)
                .model(OpenAiApi.ChatModel.GPT_4_O_MINI)
                .build();
        return new Prompt(prompt, chatOptions);
    }
}
