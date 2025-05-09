package io.hello.demo;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EnvironmentCheck {

    private final Environment env;

    public EnvironmentCheck(Environment env) {
        this.env = env;
    }

    // java -Durl=devdb -Dusername=dev_user -Dpassword=dev_pw -jar app.jar --url=devdb2 --username=dev_user2 --password=dev_pw2
    @PostConstruct
    public void init() {
        String url = env.getProperty("url");
        String username = env.getProperty("username");
        String password = env.getProperty("password");
        String driver = env.getProperty("driver");
        log.info("env url={}", url);
        log.info("env username={}", username);
        log.info("env password={}", password);
        log.info("env driver={}", driver);
    }
}
