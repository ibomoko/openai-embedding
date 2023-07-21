package com.ai.openaiembedding.model.response.embedding;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DataDTO {
    @JsonProperty(value = "embedding")
    private List<Float> embedding;
}