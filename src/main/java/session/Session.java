package session;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Getter
@Setter
@Entity
@RedisHash("session")
public class Session implements Serializable {

    private String id;
    private String sessionId;

}
