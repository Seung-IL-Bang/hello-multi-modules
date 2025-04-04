package io.hello.demo.asyncmodule.domain;


import io.hello.demo.asyncmodule.support.ThreadUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AnalyticsService {

    public void recordResultSync(boolean isSuccess) {
        // Simulate a long-running task
        ThreadUtils.sleep(3000L);
        if (isSuccess) {
            // Record successful payment
            System.out.println("[분석 서비스] 결제 성공 기록");
        } else {
            // Handle failed payment
            System.out.println("[분석 서비스] 결제 실패 기록");
        }
    }


    @Async
    public void recordResultAsync(boolean isSuccess) {
        // Simulate a long-running task
        ThreadUtils.sleep(3000L);
        if (isSuccess) {
            // Record successful payment
            System.out.println("[분석 서비스] 결제 성공 기록");
        } else {
            // Handle failed payment
            System.out.println("[분석 서비스] 결제 실패 기록");
        }
    }

}
