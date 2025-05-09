package io.hello.demo;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class OSEnv {

    public static void main(String[] args) {
        Map<String, String> envMap = System.getenv();

        for (Map.Entry<String, String> entry : envMap.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            log.info("env {}={}", key, value);
        }
    }

}
