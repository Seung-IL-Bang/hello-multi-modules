package io.hello.demo.testmodule.unittest.simplesystem.loginservice;

import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final SessionManager sessionManager;

    public LoginService(UserRepository userRepository, SessionManager sessionManager) {
        this.userRepository = userRepository;
        this.sessionManager = sessionManager;
    }

    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalStateException("패스워드가 일치하지 않습니다.");
        }

        return sessionManager.createSession(user);
    }
}
