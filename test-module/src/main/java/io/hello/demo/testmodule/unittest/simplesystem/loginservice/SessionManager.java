package io.hello.demo.testmodule.unittest.simplesystem.loginservice;

import org.springframework.stereotype.Component;

@Component
public interface SessionManager {
    String createSession(User user);
}
