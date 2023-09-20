//package session;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class SessionService {
//
//    private final SessionRepository sessionRepository;
//    private final RedisTemplate<?, ?> redisTemplate;
//
//    public boolean validateSession(Session session){
//        sessionRepository.findById(session.getId());
//        return false;
//    }
//
//    public Session saveSession(Session session){
//        return sessionRepository.save(session);
//    }
//
//    public void deleteSession(Session session){
//        sessionRepository.delete(session);
//    }
//
//}
