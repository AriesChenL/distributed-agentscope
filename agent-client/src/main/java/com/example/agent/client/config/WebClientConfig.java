package com.example.agent.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient 配置
 */
@Configuration
public class WebClientConfig {
    
    @Value("${agent.server.url:http://localhost:8080}")
    private String serverUrl;
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(serverUrl)
                .build();
    }
}
