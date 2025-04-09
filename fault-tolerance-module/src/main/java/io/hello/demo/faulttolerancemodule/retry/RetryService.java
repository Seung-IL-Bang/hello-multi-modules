package io.hello.demo.faulttolerancemodule.retry;

import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.retry.annotation.Retry;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RetryService {

    private static final int MAX_ATTEMPS = 3;
    private static final int WAIT_DURATION = 1000;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final String SIMPLE_RETRY_CONFIG = "simpleRetryConfig";

    private final RetryRegistry retryRegistry;

    public RetryService(RetryRegistry retryRegistry) {
        this.retryRegistry = retryRegistry;
    }

    @PostConstruct
    public void registerEventListener() {
        this.retryRegistry.getEventPublisher().onEvent(event -> log.info("Retry event: " + event.getEventType()));
    }

    public String processWithoutResilience4j(String param) {
        String result = null;

        int retryCount = 0;

        while (retryCount++ < MAX_ATTEMPS) {
            try {
                log.info("callAnotherServer() attempt: " + retryCount);
                result = callAnotherServer(param);
                break; // 성공하면 break
            } catch (RetryException e) {
                log.info("Retrying... " + retryCount);

                if (retryCount >= MAX_ATTEMPS) {
                    // 최대 재시도 횟수 초과 시 fallback 호출
                    result = fallback(param, e);
                    break;
                }

                try {
                    Thread.sleep(WAIT_DURATION); // 대기 후 재시도
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Thread was interrupted", ie);
                }
            }
        }

        return result;
    }

    @Retry(name = SIMPLE_RETRY_CONFIG, fallbackMethod = "fallback")
    public String processWithResilience4j(String p) {
        return callAnotherServer(p);
    }

    private String fallback(String param, Exception ex) {
        // retry에 전부 실패해야 fallback이 실행
        log.info("fallback! your request is " + param);
        return "Recovered: " + ex.toString();
    }

    private String callAnotherServer(String param) {
        // retry exception은 retry된다.
        if ("a".equals(param)) throw new RetryException("record exception");

        return "Param: " + param;
        // 그 외 exception은 retry하지 않고 바로 예외가 클라이언트에게 전달된다.
    }

}
