package com.dizzycode.dizzycode;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;

@SpringBootApplication(exclude = {RedisRepositoriesAutoConfiguration.class, RedisAutoConfiguration.class})
public class DizzycodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DizzycodeApplication.class, args);
    }
}
