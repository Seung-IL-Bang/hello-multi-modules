package io.hello.demo.sseapi;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SseService {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    // 사용자별 SSE 연결 생성
    public SseEmitter createEmitter(String transactionId) {

        SseEmitter emitter = new SseEmitter(30000L); // 60초 후 타임아웃

        // 연결 완료 이벤트
        emitter.onCompletion(() -> removeEmitter(transactionId));

        // 타임아웃 이벤트
        emitter.onTimeout(() -> removeEmitter(transactionId));

        // 에러 이벤트
        emitter.onError(e -> removeEmitter(transactionId));

        try {
            emitter.send(SseEmitter.event()
                    .name("connect")
                    .data("연결되었습니다."));
        } catch (IOException e) {
            emitter.completeWithError(e);
            return emitter;
        }

        // 사용자 ID로 emitter 저장
        emitters.put(transactionId, emitter);
        return emitter;
    }

    public void sendEvent(PaymentResultEvent event) {
        SseEmitter emitter = emitters.getOrDefault(event.userId(), null);

        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("payment-result")
                        .data(event));
            } catch (IOException e) {
                removeEmitter(event.userId()); // 에러 발생 시 emitter 제거
            }
        }
    }

    // 모든 사용자에게 이벤트 브로드캐스트 (필요시)
    public void broadcastEvent(PaymentResultEvent event) {
        emitters.forEach((userId, emitter) -> {
            try {
                emitter.send(SseEmitter.event()
                        .name("broadcast")
                        .data(event));
            } catch (IOException e) {
                emitters.remove(userId);
            }
        });
    }

    private void removeEmitter(String transactionId) {
        emitters.remove(transactionId);
    }


}
