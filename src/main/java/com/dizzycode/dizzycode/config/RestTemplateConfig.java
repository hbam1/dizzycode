package com.dizzycode.dizzycode.config;

import com.dizzycode.dizzycode.room.infrastructure.RoomIndexer;
import com.dizzycode.dizzycode.room.infrastructure.RoomIndexerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
