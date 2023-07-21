package com.ai.openaiembedding.model.request.topic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopicCreateRequest {
    @NotEmpty(message = "Topic is required")
    private String topicName;
    @NotEmpty(message = "At least 3 post is required")
    private List<String> posts;

}
