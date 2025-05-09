package io.hello.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class JavaSystemProperties {

    // java -Durl=devdb -Dusername=dev_user -Dpassword=dev_pw -jar app.jar
    public static void main(String[] args) {
        Properties properties = System.getProperties(); // Similar to Map<K, V>
        for (Object key : properties.keySet()) {
            log.info("property {}={}", key, properties.get(key));
        }

        String url = System.getProperty("url");
        String username = System.getProperty("username");
        String password = System.getProperty("password");

        log.info("url={}", url);
        log.info("username={}", username);
        log.info("password={}", password);
    }

}
