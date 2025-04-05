package io.hello.demo.testmodule.aggregationsystem;

import io.hello.demo.testmodule.aggregationsystem.api.v1.StatisticsController;
import io.hello.demo.testmodule.aggregationsystem.domain.Period;
import io.hello.demo.testmodule.aggregationsystem.api.v1.request.StatisticsRequestDto;
import io.hello.demo.testmodule.aggregationsystem.api.v1.response.StatisticsResponseDto;
import io.hello.demo.testmodule.aggregationsystem.domain.*;
import io.hello.demo.testmodule.aggregationsystem.storage.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Random;

public class StatisticsApplication {

    public static void main(String[] args) {
        // 저장소 초기화
        InMemoryPaymentRepository repository = new InMemoryPaymentRepository();
        StatisticsCalculatorFactory statisticsCalculatorFactory = new StatisticsCalculatorFactory();

        // 테스트 데이터 생성
        generateTestData(repository);

        // 서비스 및 컨트롤러 초기화
        StatisticsService service = new StatisticsService(repository, statisticsCalculatorFactory);
        StatisticsController controller = new StatisticsController(service);

        // 일별 결제 금액 통계 요청
        StatisticsRequestDto amountRequest = new StatisticsRequestDto();
        amountRequest.setMerchantId("MERCHANT-001");
        amountRequest.setStatisticType(StatisticsType.PAYMENT_AMOUNT);
        amountRequest.setPeriod(Period.DAILY);
        amountRequest.setStartDate(LocalDate.of(2023, 6, 1));
        amountRequest.setEndDate(LocalDate.of(2023, 6, 30));
        amountRequest.setGroupBy(Arrays.asList(StatisticsGroupType.PAYMENT_METHOD, StatisticsGroupType.PRODUCT_CATEGORY));

        System.out.println("\n=== 일별 결제 금액 통계 ===");
        StatisticsResponseDto amountResponse = controller.getStatistics(amountRequest);
        printResponse(amountResponse);

        // 결제 수단별 비율 통계 요청
        StatisticsRequestDto ratioRequest = new StatisticsRequestDto();
        ratioRequest.setMerchantId("MERCHANT-001");
        ratioRequest.setStatisticType(StatisticsType.PAYMENT_METHOD_RATIO);
        ratioRequest.setPeriod(Period.MONTHLY);
        ratioRequest.setStartDate(LocalDate.of(2023, 6, 1));
        ratioRequest.setEndDate(LocalDate.of(2023, 6, 30));

        System.out.println("\n=== 월별 결제 수단 비율 통계 ===");
        StatisticsResponseDto ratioResponse = controller.getStatistics(ratioRequest);
        printResponse(ratioResponse);
    }

    private static void generateTestData(InMemoryPaymentRepository repository) {
        Random random = new Random(42);
        PaymentMethod[] paymentMethods = PaymentMethod.values();
        ProductCategory[] productCategories = ProductCategory.values();
        PaymentStatus[] statuses = PaymentStatus.values();

        for (int i = 0; i < 1000; i++) {
            Payment payment = new Payment();
            payment.setId("PAYMENT-" + i);
            payment.setMerchantId("MERCHANT-001");
            payment.setAmount(new BigDecimal(random.nextInt(100000) + 1000));
            payment.setPaymentMethod(paymentMethods[random.nextInt(paymentMethods.length)]);
            payment.setProductCategory(productCategories[random.nextInt(productCategories.length)]);
            payment.setStatus(statuses[random.nextInt(10) < 9 ? 0 : 1]);  // 90% 승인, 10% 취소

            // 2023년 6월 내 랜덤 시간
            int day = random.nextInt(30) + 1;
            int hour = random.nextInt(24);
            int minute = random.nextInt(60);
            payment.setCreatedAt(LocalDateTime.of(2023, 6, day, hour, minute));

            repository.save(payment);
        }

        System.out.println("Generated 1000 test payment records");
    }

    private static void printResponse(StatisticsResponseDto response) {
        System.out.println("통계 유형: " + response.getStatisticType());
        System.out.println("기간 단위: " + response.getPeriod());

        for (PeriodData periodData : response.getData()) {
            System.out.println("\n날짜: " + periodData.getDate());

            for (DetailData detail : periodData.getDetails()) {
                System.out.print("  ");
                if (detail.getPaymentMethod() != null) {
                    System.out.print("결제 수단: " + detail.getPaymentMethod() + ", ");
                }
                if (detail.getProductCategory() != null) {
                    System.out.print("상품 카테고리: " + detail.getProductCategory() + ", ");
                }
                if (detail.getAmount() != null) {
                    System.out.print("금액: " + detail.getAmount() + ", ");
                }
                if (detail.getCount() != null) {
                    System.out.print("건수: " + detail.getCount() + ", ");
                }
                if (detail.getAdditionalData() != null && detail.getAdditionalData().get("ratio") != null) {
                    System.out.print("비율: " + detail.getAdditionalData().get("ratio") + "%");
                }
                System.out.println();
            }
        }
    }
}
