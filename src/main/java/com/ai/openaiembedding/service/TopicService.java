package com.ai.openaiembedding.service;

import com.ai.openaiembedding.model.request.topic.TopicCreateRequest;
import com.ai.openaiembedding.model.response.topic.LessRelatedPostResponse;
import com.ai.openaiembedding.model.response.topic.TopicCreateResponse;

public interface TopicService {
    TopicCreateResponse createTopic(TopicCreateRequest topicCreateRequest);
    LessRelatedPostResponse getLessRelatedPost(String topicId);
}
