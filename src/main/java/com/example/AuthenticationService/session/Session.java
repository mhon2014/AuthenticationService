package com.example.AuthenticationService.session;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;


@Data
@EqualsAndHashCode
@RedisHash()
public class Session implements Serializable {

    private String id;
    private String sessionId;

}
