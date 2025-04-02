package io.hello.demo.sseapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/sse")
public class SseController {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final SseService sseService;

    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    // SSE 연결 엔드포인트
    @GetMapping(value = "/connect/{transactionId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter connect(@PathVariable("transactionId") String transactionId) {
        log.info("SSE 연결 요청: {}", transactionId);
        return sseService.createEmitter(transactionId);
    }

    // 결제 결과 알림 전송 (백엔드 시스템에서 호출)
    @PostMapping("/send/payment-result")
    public void sendPaymentResult(@RequestBody PaymentResultEvent event) {
        log.info("결제 결과 전송: {}", event);
        sseService.sendEvent(event);
    }
}

