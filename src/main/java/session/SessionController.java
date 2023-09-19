package session;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sessions")
public class SessionController {

    private final SessionRepository sessionRepository;
    private final SessionService sessionService;

    @GetMapping("/all")
    public List<Session> getAll(){
        return sessionRepository.findAll();
    }

}
