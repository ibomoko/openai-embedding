package com.ai.openaiembedding.model.request.embedding;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmbeddingRequest {
    @JsonProperty(value = "model")
    private String model;
    @JsonProperty(value = "input")
    private String post;
}
