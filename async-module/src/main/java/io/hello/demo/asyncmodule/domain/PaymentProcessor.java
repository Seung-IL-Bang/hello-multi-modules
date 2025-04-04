package io.hello.demo.asyncmodule.domain;

import io.hello.demo.asyncmodule.support.ThreadUtils;
import org.springframework.stereotype.Component;

@Component
public class PaymentProcessor {

    public boolean process(String paymentId) {
        // Simulate payment processing
        System.out.println("[결제 처리 서비스] 결제가 진행중 입니다. ID: " + paymentId);

        // Simulate some delay
        ThreadUtils.sleep(1000L);

        if (Math.random() > 0.2) {
            // Simulate a successful payment processing
            System.out.println("[결제 처리 서비스] 결제 완료 ID " + paymentId);
            return true;
        } else {
            // Simulate a failed payment processing
            System.out.println("[결제 처리 서비스] 결제 실패 ID" + paymentId);
            return false;
        }
    }

}
