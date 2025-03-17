package com.project.blogmate.api;

import com.project.blogmate.domain.TransformService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransformController {
    private final TransformService transformService;

    public TransformController(TransformService transformService) {
        this.transformService = transformService;
    }

    @PostMapping("/blog/transform")
    public TransformResponse transform(@RequestBody TransformRequest request) {
        return TransformResponse.from(transformService.transform(request.toRawData()));
    }
}
