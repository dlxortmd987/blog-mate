package com.project.blogmate.infra.openai;

import com.project.blogmate.domain.ContentLength;
import com.project.blogmate.domain.ContentRawData;
import com.project.blogmate.domain.ContentType;
import com.project.blogmate.domain.TransformResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=test")
class OpenAiBlogTransformServiceTest {
    @Autowired
    private OpenAiBlogTransformService openAiBlogTransformService;

    @Test
    void transform() {
        TransformResult transformResult = openAiBlogTransformService.transform(new ContentRawData(ContentLength.MIN, ContentType.PROGRAMMING, "SpringBoot Test"));
        System.out.println("transformResult = " + transformResult);
    }
}