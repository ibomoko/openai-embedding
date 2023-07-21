package com.ai.openaiembedding.constants;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class OpenAIConstants {

    @Value("${openai.api-key}")
    private String apiKey;

    @Value("${openai.embedding-model}")
    private String embeddingModel;

    @Value("${openai.embedding-url}")
    private String embeddingUrl;
}
