package io.hello.demo.asyncmodule.domain;

import io.hello.demo.asyncmodule.support.ThreadUtils;
import io.hello.demo.asyncmodule.support.error.AsyncException;
import io.hello.demo.asyncmodule.support.error.ErrorType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {


    public void notifyResultSync(boolean isSuccess) {
        // Simulate a long-running email sending process
        ThreadUtils.sleep(3000L);
        if (isSuccess) {
            System.out.println("[알림 서비스] 결제 성공 알림");
        } else {
            System.out.println("[알림 서비스] 결제 실패 알림");
        }
    }


    @Async
    public void notifyResultAsync(boolean isSuccess) {
        // Simulate a long-running email sending process
        ThreadUtils.sleep(3000L);
        if (isSuccess) {
            System.out.println("[알림 서비스] 결제 성공 알림");
        } else {
            System.out.println("[알림 서비스] 결제 실패 알림");
        }
    }

    @Async
    public void notifyResultUncaughtAsyncException() {
        // Simulate a long-running email sending process
        ThreadUtils.sleep(3000L);
        throw new RuntimeException(ErrorType.FAILED_NOFITY_PAYMENT_RESULT.getMessage());
    }

    @Async
    public void notifyResultCaughtAsyncException() {
        // Simulate a long-running email sending process
        ThreadUtils.sleep(3000L);
        throw new AsyncException(ErrorType.FAILED_NOFITY_PAYMENT_RESULT);
    }

}
