package com.ai.openaiembedding.controller;

import com.ai.openaiembedding.model.request.topic.TopicCreateRequest;
import com.ai.openaiembedding.model.response.topic.LessRelatedPostResponse;
import com.ai.openaiembedding.model.response.topic.TopicCreateResponse;
import com.ai.openaiembedding.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/api/topic")
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<TopicCreateResponse> createTopic(@Valid @RequestBody TopicCreateRequest topicCreateRequest) {
        return ResponseEntity.ok(topicService.createTopic(topicCreateRequest));
    }

    @GetMapping("/{id}/detect-anomaly")
    public ResponseEntity<LessRelatedPostResponse> detectAnomalyPost(@PathVariable(value = "id") String topicId) {
        return ResponseEntity.ok(topicService.getLessRelatedPost(topicId));
    }

}
