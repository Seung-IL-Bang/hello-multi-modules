package io.hello.demo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@RestController
public class TrafficController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/cpu/stress")
    public String cpuStress() {
        log.info("CPU stress test started");
        // Simulate CPU stress
        long value = 0;
        for (long i = 0; i < 10_000_000_000L; i++) {
            value++;
        }
        log.info("CPU stress test completed");
        return "CPU stress test completed";
    }

    @GetMapping("/jvm/memory/accumulate")
    public String jvmMemoryAccumulate() {
        log.info("JVM memory accumulation test started");
        // Simulate memory accumulation
        String[] memoryHog = new String[10_000_000];
        for (int i = 0; i < memoryHog.length; i++) {
            memoryHog[i] = "Memory hog " + i;
        }
        log.info("JVM memory accumulation test completed");
        return "JVM memory accumulation test completed";
    }

    @GetMapping("/error-log")
    public String errorLog() {
        log.error("This is an error log message");
        return "Error log message generated";
    }

    @GetMapping("/jdbc")
    public String jdbc() throws SQLException {
        log.info("JDBC operation started");
        // Simulate JDBC operation
        Connection conn = dataSource.getConnection();
        log.info("connection info={}", conn);
        return "JDBC operation completed";
    }
}
