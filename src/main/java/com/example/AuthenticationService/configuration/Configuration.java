package com.example.AuthenticationService.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@org.springframework.context.annotation.Configuration
public class Configuration {

    // https://developer.redis.com/develop/java/spring
    // https://developer.redis.com/develop/java/redis-and-spring-course/lesson_3/
    @Bean
    public RedisTemplate<?, ?> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<?, ?> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }

}
