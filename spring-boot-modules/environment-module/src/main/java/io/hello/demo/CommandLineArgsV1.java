package io.hello.demo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandLineArgsV1 {

    // java -jar app.jar dataA dataB --url=devdb username=dev_user password=dev_pw "hello world"
    public static void main(String[] args) {
        for (String arg : args) {
            log.info("arg={}", arg);
        }
    }

}
