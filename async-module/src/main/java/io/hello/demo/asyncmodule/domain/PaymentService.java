package io.hello.demo.asyncmodule.domain;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final AnalyticsService analyticsService;
    private final NotificationService notificationService;
    private final PaymentProcessor paymentProcessor;

    public PaymentService(AnalyticsService analyticsService,
                          NotificationService notificationService,
                          PaymentProcessor paymentProcessor) {
        this.analyticsService = analyticsService;
        this.notificationService = notificationService;
        this.paymentProcessor = paymentProcessor;
    }

    public void processPaymentAsync(String paymentId) {
        // Simulate payment processing
        boolean result = paymentProcessor.process(paymentId);

        notificationService.notifyResultAsync(result);
        analyticsService.recordResultAsync(result);
    }

    public void processPaymentSync(String paymentId) {
        // Simulate payment processing
        boolean result = paymentProcessor.process(paymentId);

        notificationService.notifyResultSync(result);
        analyticsService.recordResultSync(result);
    }

    public void processPaymentUncaughtAsyncException(String paymentId) {
        // Simulate payment processing
        boolean result = paymentProcessor.process(paymentId);
        notificationService.notifyResultUncaughtAsyncException();
        analyticsService.recordResultAsync(result);
    }

    public void processPaymentCaughtAsyncException(String paymentId) {
        // Simulate payment processing
        boolean result = paymentProcessor.process(paymentId);
        notificationService.notifyResultCaughtAsyncException();
        analyticsService.recordResultAsync(result);
    }

}
