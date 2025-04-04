package io.hello.demo.asyncmodule.config;

import io.hello.demo.asyncmodule.support.error.AsyncException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import java.lang.reflect.Method;

public class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public void handleUncaughtException(Throwable ex, Method method, Object... params) {
        if (ex instanceof AsyncException ae) {
            switch (ae.getErrorType().getLogLevel()) {
                case ERROR -> log.error("Async error occurred in method: {} with params: {} with message: {}", method.getName(), params, ae.getMessage());
                case WARN -> log.warn("Async warning occurred in method: {} with params: {} with message: {}", method.getName(), params, ae.getMessage());
                default -> log.info("Async info occurred in method: {} with params: {} with message: {}", method.getName(), params, ae.getMessage());
            }
        } else {
            log.error("Unexpected error occurred in method: {} with params: {} with message: {}", method.getName(), params, ex.getMessage());
        }
    }
}
