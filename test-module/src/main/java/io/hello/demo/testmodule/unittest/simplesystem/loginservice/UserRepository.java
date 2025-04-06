package io.hello.demo.testmodule.unittest.simplesystem.loginservice;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {
    Optional<User> findByEmail(String email);
}
