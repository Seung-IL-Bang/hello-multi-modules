package io.hello.demo.testmodule.unittest.settlementsystem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SettlementServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private SettlementRepository settlementRepository;

    @Mock
    private FeeCalculator feeCalculator;

    @InjectMocks
    private SettlementService settlementService;

    @Captor
    private ArgumentCaptor<Settlement> settlementCaptor;

    private Merchant merchant;
    private List<Transaction> transactions;
    private Settlement createdSettlement;
    private Settlement approvedSettlement;
    private LocalDate settlementDate;

    @BeforeEach
    void setUp() {
        // 가맹점 정보 설정
        merchant = new Merchant();
        merchant.setId("MERCHANT-123");
        merchant.setName("Test Merchant");
        merchant.setBusinessNumber("123-45-67890");
        merchant.setFeeRate(new BigDecimal("0.03")); // 3% 수수료
        merchant.setBankCode("004");
        merchant.setAccountNumber("1234567890");

        // 정산 대상 거래 내역 설정
        transactions = new ArrayList<>();
        settlementDate = LocalDate.of(2023, 7, 1);

        // 첫 번째 거래
        Transaction transaction1 = new Transaction();
        transaction1.setId("TRANSACTION-1");
        transaction1.setMerchantId("MERCHANT-123");
        transaction1.setAmount(new BigDecimal("10000"));
        transaction1.setPaymentId("PAYMENT-1");
        transaction1.setCreatedAt(settlementDate.atTime(10, 30));
        transactions.add(transaction1);

        // 두 번째 거래
        Transaction transaction2 = new Transaction();
        transaction2.setId("TRANSACTION-2");
        transaction2.setMerchantId("MERCHANT-123");
        transaction2.setAmount(new BigDecimal("20000"));
        transaction2.setPaymentId("PAYMENT-2");
        transaction2.setCreatedAt(settlementDate.atTime(14, 15));
        transactions.add(transaction2);

        // 생성된 정산 정보 설정
        createdSettlement = new Settlement();
        createdSettlement.setId("SETTLEMENT-12345678");
        createdSettlement.setMerchantId("MERCHANT-123");
        createdSettlement.setSettlementDate(settlementDate);
        createdSettlement.setTotalAmount(new BigDecimal("30000")); // 10000 + 20000
        createdSettlement.setFeeAmount(new BigDecimal("900")); // 30000 * 0.03
        createdSettlement.setSettlementAmount(new BigDecimal("29100")); // 30000 - 900
        createdSettlement.setTransactionCount(2);
        createdSettlement.setStatus(SettlementStatus.CREATED);
        createdSettlement.setCreatedAt(LocalDateTime.now());

        // 승인된 정산 정보 설정
        approvedSettlement = new Settlement();
        approvedSettlement.setId("SETTLEMENT-12345678");
        approvedSettlement.setMerchantId("MERCHANT-123");
        approvedSettlement.setSettlementDate(settlementDate);
        approvedSettlement.setTotalAmount(new BigDecimal("30000"));
        approvedSettlement.setFeeAmount(new BigDecimal("900"));
        approvedSettlement.setSettlementAmount(new BigDecimal("29100"));
        approvedSettlement.setTransactionCount(2);
        approvedSettlement.setStatus(SettlementStatus.APPROVED);
        approvedSettlement.setCreatedAt(LocalDateTime.now());
        approvedSettlement.setApprovedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("정상적인 정산 생성 테스트")
    void createSettlement_WithValidData_ShouldCreateSettlement() {
        // Given
        given(merchantRepository.findById("MERCHANT-123")).willReturn(Optional.of(merchant));
        given(settlementRepository.existsByMerchantIdAndSettlementDate("MERCHANT-123", settlementDate))
                .willReturn(false);

        LocalDateTime startDateTime = settlementDate.atStartOfDay();
        LocalDateTime endDateTime = settlementDate.plusDays(1).atStartOfDay();

        given(transactionRepository.findByMerchantIdAndCreatedAtBetween("MERCHANT-123", startDateTime, endDateTime))
                .willReturn(transactions);

        given(feeCalculator.calculateFee(merchant, new BigDecimal("30000")))
                .willReturn(new BigDecimal("900"));

        given(settlementRepository.save(any(Settlement.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // When
        Settlement result = settlementService.createSettlement("MERCHANT-123", settlementDate);

        // Then
        verify(merchantRepository).findById("MERCHANT-123");
        verify(settlementRepository).existsByMerchantIdAndSettlementDate("MERCHANT-123", settlementDate);
        verify(transactionRepository).findByMerchantIdAndCreatedAtBetween("MERCHANT-123", startDateTime, endDateTime);
        verify(feeCalculator).calculateFee(merchant, new BigDecimal("30000"));
        verify(settlementRepository).save(settlementCaptor.capture());

        Settlement capturedSettlement = settlementCaptor.getValue();

        assertNotNull(capturedSettlement.getId());
        assertEquals("MERCHANT-123", capturedSettlement.getMerchantId());
        assertEquals(settlementDate, capturedSettlement.getSettlementDate());
        assertEquals(new BigDecimal("30000"), capturedSettlement.getTotalAmount());
        assertEquals(new BigDecimal("900"), capturedSettlement.getFeeAmount());
        assertEquals(new BigDecimal("29100"), capturedSettlement.getSettlementAmount());
        assertEquals(2, capturedSettlement.getTransactionCount());
        assertEquals(SettlementStatus.CREATED, capturedSettlement.getStatus());
        assertNotNull(capturedSettlement.getCreatedAt());
    }

    @Test
    @DisplayName("존재하지 않는 가맹점에 대한 정산 생성 시도 시 예외 발생 테스트")
    void createSettlement_WithNonExistingMerchant_ShouldThrowException() {
        // Given
        given(merchantRepository.findById("NON-EXISTING-ID")).willReturn(Optional.empty());

        // When & Then
        MerchantNotFoundException exception = assertThrows(
                MerchantNotFoundException.class,
                () -> settlementService.createSettlement("NON-EXISTING-ID", settlementDate)
        );

        assertTrue(exception.getMessage().contains("Merchant not found"));

        verify(merchantRepository).findById("NON-EXISTING-ID");
        verify(settlementRepository, never()).existsByMerchantIdAndSettlementDate(anyString(), any(LocalDate.class));
        verify(transactionRepository, never())
                .findByMerchantIdAndCreatedAtBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(settlementRepository, never()).save(any(Settlement.class));
    }

    @Test
    @DisplayName("이미 정산이 존재하는 날짜에 정산 생성 시도 시 예외 발생 테스트")
    void createSettlement_WithExistingSettlement_ShouldThrowException() {
        // Given
        given(merchantRepository.findById("MERCHANT-123")).willReturn(Optional.of(merchant));
        given(settlementRepository.existsByMerchantIdAndSettlementDate("MERCHANT-123", settlementDate))
                .willReturn(true);

        // When & Then
        InvalidSettlementException exception = assertThrows(
                InvalidSettlementException.class,
                () -> settlementService.createSettlement("MERCHANT-123", settlementDate)
        );

        assertTrue(exception.getMessage().contains("Settlement already exists"));

        verify(merchantRepository).findById("MERCHANT-123");
        verify(settlementRepository).existsByMerchantIdAndSettlementDate("MERCHANT-123", settlementDate);
        verify(transactionRepository, never())
                .findByMerchantIdAndCreatedAtBetween(anyString(), any(LocalDateTime.class), any(LocalDateTime.class));
        verify(settlementRepository, never()).save(any(Settlement.class));
    }

    @Test
    @DisplayName("거래 내역이 없는 날짜에 정산 생성 시도 시 예외 발생 테스트")
    void createSettlement_WithNoTransactions_ShouldThrowException() {
        // Given
        given(merchantRepository.findById("MERCHANT-123")).willReturn(Optional.of(merchant));
        given(settlementRepository.existsByMerchantIdAndSettlementDate("MERCHANT-123", settlementDate))
                .willReturn(false);

        LocalDateTime startDateTime = settlementDate.atStartOfDay();
        LocalDateTime endDateTime = settlementDate.plusDays(1).atStartOfDay();

        given(transactionRepository.findByMerchantIdAndCreatedAtBetween("MERCHANT-123", startDateTime, endDateTime))
                .willReturn(new ArrayList<>()); // 빈 거래 목록

        // When & Then
        InvalidSettlementException exception = assertThrows(
                InvalidSettlementException.class,
                () -> settlementService.createSettlement("MERCHANT-123", settlementDate)
        );

        assertTrue(exception.getMessage().contains("No transactions found"));

        verify(merchantRepository).findById("MERCHANT-123");
        verify(settlementRepository).existsByMerchantIdAndSettlementDate("MERCHANT-123", settlementDate);
        verify(transactionRepository).findByMerchantIdAndCreatedAtBetween("MERCHANT-123", startDateTime, endDateTime);
        verify(settlementRepository, never()).save(any(Settlement.class));
    }

    @Test
    @DisplayName("정상적인 정산 승인 테스트")
    void approveSettlement_WithCreatedSettlement_ShouldApproveSettlement() {
        // Given
        given(settlementRepository.findById("SETTLEMENT-12345678")).willReturn(Optional.of(createdSettlement));

        Settlement approvedSettlementResult = new Settlement();
        approvedSettlementResult.setId("SETTLEMENT-12345678");
        approvedSettlementResult.setStatus(SettlementStatus.APPROVED);
        approvedSettlementResult.setApprovedAt(LocalDateTime.now());

        given(settlementRepository.save(any(Settlement.class))).willReturn(approvedSettlementResult);

        // When
        Settlement result = settlementService.approveSettlement("SETTLEMENT-12345678");

        // Then
        verify(settlementRepository).findById("SETTLEMENT-12345678");
        verify(settlementRepository).save(settlementCaptor.capture());

        Settlement capturedSettlement = settlementCaptor.getValue();
        assertEquals(SettlementStatus.APPROVED, capturedSettlement.getStatus());
        assertNotNull(capturedSettlement.getApprovedAt());

        assertEquals(SettlementStatus.APPROVED, result.getStatus());
    }

    @Test
    @DisplayName("이미 승인된 정산 승인 시도 시 예외 발생 테스트")
    void approveSettlement_WithAlreadyApprovedSettlement_ShouldThrowException() {
        // Given
        given(settlementRepository.findById("SETTLEMENT-12345678")).willReturn(Optional.of(approvedSettlement));

        // When & Then
        InvalidSettlementException exception = assertThrows(
                InvalidSettlementException.class,
                () -> settlementService.approveSettlement("SETTLEMENT-12345678")
        );

        assertTrue(exception.getMessage().contains("Settlement cannot be approved"));

        verify(settlementRepository).findById("SETTLEMENT-12345678");
        verify(settlementRepository, never()).save(any(Settlement.class));
    }

    @Test
    @DisplayName("정상적인 정산 완료 테스트")
    void completeSettlement_WithApprovedSettlement_ShouldCompleteSettlement() {
        // Given
        given(settlementRepository.findById("SETTLEMENT-12345678")).willReturn(Optional.of(approvedSettlement));

        Settlement completedSettlement = new Settlement();
        completedSettlement.setId("SETTLEMENT-12345678");
        completedSettlement.setStatus(SettlementStatus.COMPLETED);
        completedSettlement.setCompletedAt(LocalDateTime.now());

        given(settlementRepository.save(any(Settlement.class))).willReturn(completedSettlement);

        // When
        Settlement result = settlementService.completeSettlement("SETTLEMENT-12345678");

        // Then
        verify(settlementRepository).findById("SETTLEMENT-12345678");
        verify(settlementRepository).save(settlementCaptor.capture());

        Settlement capturedSettlement = settlementCaptor.getValue();
        assertEquals(SettlementStatus.COMPLETED, capturedSettlement.getStatus());
        assertNotNull(capturedSettlement.getCompletedAt());

        assertEquals(SettlementStatus.COMPLETED, result.getStatus());
    }

    @Test
    @DisplayName("승인되지 않은 정산 완료 시도 시 예외 발생 테스트")
    void completeSettlement_WithNonApprovedSettlement_ShouldThrowException() {
        // Given
        given(settlementRepository.findById("SETTLEMENT-12345678")).willReturn(Optional.of(createdSettlement));

        // When & Then
        InvalidSettlementException exception = assertThrows(
                InvalidSettlementException.class,
                () -> settlementService.completeSettlement("SETTLEMENT-12345678")
        );

        assertTrue(exception.getMessage().contains("Settlement cannot be completed"));

        verify(settlementRepository).findById("SETTLEMENT-12345678");
        verify(settlementRepository, never()).save(any(Settlement.class));
    }

    @Test
    @DisplayName("가맹점 및 기간별 정산 조회 테스트")
    void getSettlementsByMerchantAndDateRange_ShouldReturnSettlements() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = LocalDate.of(2023, 7, 31);

        List<Settlement> expectedSettlements = Arrays.asList(createdSettlement, approvedSettlement);

        given(settlementRepository.findByMerchantIdAndSettlementDateBetween("MERCHANT-123", startDate, endDate))
                .willReturn(expectedSettlements);

        // When
        List<Settlement> result = settlementService.getSettlementsByMerchantAndDateRange("MERCHANT-123", startDate, endDate);

        // Then
        assertEquals(2, result.size());
        verify(settlementRepository).findByMerchantIdAndSettlementDateBetween("MERCHANT-123", startDate, endDate);
    }

}