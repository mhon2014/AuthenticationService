package com.example.AuthenticationService.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfig {

    // https://developer.redis.com/develop/java/spring
    // https://developer.redis.com/develop/java/redis-and-spring-course/lesson_3/
    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

}
