package com.ai.openaiembedding.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .servers(getServers())
                .info(new Info().title("OpenAI Embedding"));
    }

    private List<Server> getServers() {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("http://localhost:8080"));
        return servers;
    }
}
