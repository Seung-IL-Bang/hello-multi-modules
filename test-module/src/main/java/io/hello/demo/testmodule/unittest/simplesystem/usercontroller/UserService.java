package io.hello.demo.testmodule.unittest.simplesystem.usercontroller;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    User createUser(User user);
}
