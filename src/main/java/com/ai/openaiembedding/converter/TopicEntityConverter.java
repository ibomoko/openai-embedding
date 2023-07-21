package com.ai.openaiembedding.converter;

import com.ai.openaiembedding.entity.Topic;
import com.ai.openaiembedding.model.request.topic.TopicCreateRequest;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

@Component
public class TopicEntityConverter implements Function<TopicCreateRequest, Topic> {
    @Override
    public Topic apply(TopicCreateRequest topicCreateRequest) {
        return Topic.builder()
                .createDate(new Date())
                .name(topicCreateRequest.getTopicName())
                .build();
    }
}
