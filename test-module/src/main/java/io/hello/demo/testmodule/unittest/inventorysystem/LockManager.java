package io.hello.demo.testmodule.unittest.inventorysystem;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class LockManager {

    private final Map<String, Lock> locks = new ConcurrentHashMap<>();

    public Lock getLock(String key) {
        return locks.computeIfAbsent(key, k -> new ReentrantLock());
    }
}
