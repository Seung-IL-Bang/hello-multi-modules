package io.hello.demo.testmodule.firstcome;

import io.hello.demo.testmodule.firstcome.api.v1.EventController;
import io.hello.demo.testmodule.firstcome.api.v1.request.EventParticipationRequestDto;
import io.hello.demo.testmodule.firstcome.api.v1.response.EventParticipationResponseDto;
import io.hello.demo.testmodule.firstcome.domain.*;
import io.hello.demo.testmodule.firstcome.storage.*;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventApplication {

    public static void main(String[] args) throws Exception {
        // 저장소 초기화
        EventRepository eventRepository = new InMemoryEventRepository();
        CouponRepository couponRepository = new InMemoryCouponRepository();

        // 서비스 및 컨트롤러 초기화
        EventService eventService = new EventService(eventRepository, couponRepository);
        EventController eventController = new EventController(eventService);

        // 테스트 이벤트 생성
        LocalDateTime now = LocalDateTime.now();
        Event activeEvent = new Event(
                "EVENT-001",
                "5000원 할인 쿠폰 선착순 이벤트",
                now.minusMinutes(5),  // 5분 전 시작
                now.plusHours(1),     // 1시간 후 종료
                100                  // 총 100개 쿠폰
        );

        eventRepository.save(activeEvent);

        // 동시에 500명의 사용자가 참여하는 시뮬레이션
        simulateConcurrentParticipation(eventController, activeEvent.getEventId(), 500);
    }

    private static void simulateConcurrentParticipation(
            EventController controller, String eventId, int userCount) throws Exception {

        ExecutorService executor = Executors.newFixedThreadPool(16);
        CountDownLatch latch = new CountDownLatch(userCount);

        int[] successCount = {0};

        System.out.println("Starting simulation with " + userCount + " concurrent users...");
        long startTime = System.currentTimeMillis();

        // 각 사용자별로 별도 쓰레드에서 이벤트 참여 요청
        for (int i = 0; i < userCount; i++) {
            final int userId = i;
            executor.submit(() -> {
                try {
                    EventParticipationRequestDto request = new EventParticipationRequestDto();
                    request.setEventId(eventId);
                    request.setUserId("USER-" + userId);
                    request.setTimestamp(LocalDateTime.now());

                    EventParticipationResponseDto response = controller.participateInEvent(request);

                    if ("SUCCESS".equals(response.getResult())) {
                        synchronized (successCount) {
                            successCount[0]++;
                        }
                        System.out.println("User " + userId + " got coupon: " + response.getCouponId());
                    } else {
                        System.out.println("User " + userId + " failed: " + response.getMessage());
                    }
                } finally {
                    latch.countDown();
                }
            });
        }

        // 모든 요청이 완료될 때까지 대기
        latch.await();
        executor.shutdown();

        long endTime = System.currentTimeMillis();

        System.out.println("\nSimulation completed in " + (endTime - startTime) + "ms");
        System.out.println("Successful participants: " + successCount[0]);
        System.out.println("Failed participants: " + (userCount - successCount[0]));
    }
}
