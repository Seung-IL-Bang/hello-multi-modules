package io.hello.demo.testmodule.firstcome.domain;

import io.hello.demo.testmodule.firstcome.storage.CouponRepository;
import io.hello.demo.testmodule.firstcome.storage.Event;
import io.hello.demo.testmodule.firstcome.storage.EventRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EventService {

    private final EventRepository eventRepository;
    private final List<EventProcessingStrategy> strategies;

    // 이벤트별 락 관리 (동시성 제어)
    private final ConcurrentLockManager lockManager = new ConcurrentLockManager();

    public EventService(EventRepository eventRepository, CouponRepository couponRepository) {
        this.eventRepository = eventRepository;

        // 이벤트 처리 전략 초기화
        this.strategies = new CopyOnWriteArrayList<>();
        this.strategies.add(new PendingEventStrategy());
        this.strategies.add(new ActiveEventStrategy(couponRepository, 5000, 30));
        this.strategies.add(new EndedEventStrategy());
    }

    public EventParticipationResult participateInEvent(EventParticipationRequest request) {
        // 입력 유효성 검증
        if (request.getEventId() == null || request.getEventId().isEmpty()) {
            return EventParticipationResult.failure("이벤트 ID가 필요합니다.");
        }

        if (request.getUserId() == null || request.getUserId().isEmpty()) {
            return EventParticipationResult.failure("사용자 ID가 필요합니다.");
        }

        // 이벤트 조회
        Optional<Event> eventOpt = eventRepository.findById(request.getEventId());
        if (eventOpt.isEmpty()) {
            return EventParticipationResult.failure("존재하지 않는 이벤트입니다.");
        }

        Event event = eventOpt.get();

        // 이벤트 상태 업데이트
        event.updateStatus();

        // 해당 이벤트에 대한 락 획득
        Lock lock = lockManager.getLock(request.getEventId());

        try {
            lock.lock();

            // 이벤트 상태에 맞는 전략 선택 및 실행
            for (EventProcessingStrategy strategy : strategies) {
                if (strategy.canProcess(event)) {
                    return strategy.process(event, request);
                }
            }

            // 적절한 전략이 없는 경우 기본 응답
            return EventParticipationResult.failure("이벤트 처리 중 오류가 발생했습니다.");

        } finally {
            lock.unlock();
        }
    }


    // 이벤트별 락 관리를 위한 유틸리티 클래스
    private static class ConcurrentLockManager {
        private final Map<String, Lock> locks = new ConcurrentHashMap<>();

        public Lock getLock(String key) {
            return locks.computeIfAbsent(key, k -> new ReentrantLock());
        }
    }
}
