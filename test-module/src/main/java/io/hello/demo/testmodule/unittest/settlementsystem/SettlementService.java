package io.hello.demo.testmodule.unittest.settlementsystem;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SettlementService {

    private final MerchantRepository merchantRepository;
    private final TransactionRepository transactionRepository;
    private final SettlementRepository settlementRepository;
    private final FeeCalculator feeCalculator;

    public SettlementService(MerchantRepository merchantRepository,
                             TransactionRepository transactionRepository,
                             SettlementRepository settlementRepository,
                             FeeCalculator feeCalculator) {
        this.merchantRepository = merchantRepository;
        this.transactionRepository = transactionRepository;
        this.settlementRepository = settlementRepository;
        this.feeCalculator = feeCalculator;
    }

    @Transactional
    public Settlement createSettlement(String merchantId, LocalDate settlementDate) {
        // 가맹점 조회
        Merchant merchant = merchantRepository.findById(merchantId)
                .orElseThrow(() -> new MerchantNotFoundException("Merchant not found: " + merchantId));

        // 해당 날짜의 정산이 이미 존재하는지 확인
        if (settlementRepository.existsByMerchantIdAndSettlementDate(merchantId, settlementDate)) {
            throw new InvalidSettlementException("Settlement already exists for the date: " + settlementDate);
        }

        // 정산 대상 거래 내역 조회
        LocalDateTime startDateTime = settlementDate.atStartOfDay();
        LocalDateTime endDateTime = settlementDate.plusDays(1).atStartOfDay();

        List<Transaction> transactions = transactionRepository.findByMerchantIdAndCreatedAtBetween(
                merchantId, startDateTime, endDateTime);

        if (transactions.isEmpty()) {
            throw new InvalidSettlementException("No transactions found for settlement");
        }

        // 총 금액 및 수수료 계산
        BigDecimal totalAmount = calculateTotalAmount(transactions);
        BigDecimal feeAmount = feeCalculator.calculateFee(merchant, totalAmount);
        BigDecimal settlementAmount = totalAmount.subtract(feeAmount);

        // 정산 정보 생성
        Settlement settlement = new Settlement();
        settlement.setId(generateSettlementId());
        settlement.setMerchantId(merchantId);
        settlement.setSettlementDate(settlementDate);
        settlement.setTotalAmount(totalAmount);
        settlement.setFeeAmount(feeAmount);
        settlement.setSettlementAmount(settlementAmount);
        settlement.setTransactionCount(transactions.size());
        settlement.setStatus(SettlementStatus.CREATED);
        settlement.setCreatedAt(LocalDateTime.now());

        return settlementRepository.save(settlement);
    }

    @Transactional
    public Settlement approveSettlement(String settlementId) {
        Settlement settlement = settlementRepository.findById(settlementId)
                .orElseThrow(() -> new InvalidSettlementException("Settlement not found: " + settlementId));

        if (settlement.getStatus() != SettlementStatus.CREATED) {
            throw new InvalidSettlementException("Settlement cannot be approved. Current status: " + settlement.getStatus());
        }

        settlement.setStatus(SettlementStatus.APPROVED);
        settlement.setApprovedAt(LocalDateTime.now());

        return settlementRepository.save(settlement);
    }

    @Transactional
    public Settlement completeSettlement(String settlementId) {
        Settlement settlement = settlementRepository.findById(settlementId)
                .orElseThrow(() -> new InvalidSettlementException("Settlement not found: " + settlementId));

        if (settlement.getStatus() != SettlementStatus.APPROVED) {
            throw new InvalidSettlementException("Settlement cannot be completed. Current status: " + settlement.getStatus());
        }

        settlement.setStatus(SettlementStatus.COMPLETED);
        settlement.setCompletedAt(LocalDateTime.now());

        return settlementRepository.save(settlement);
    }

    @Transactional(readOnly = true)
    public List<Settlement> getSettlementsByMerchantAndDateRange(
            String merchantId, LocalDate startDate, LocalDate endDate) {
        return settlementRepository.findByMerchantIdAndSettlementDateBetween(
                merchantId, startDate, endDate);
    }

    private BigDecimal calculateTotalAmount(List<Transaction> transactions) {
        return transactions.stream()
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String generateSettlementId() {
        return "SETTLEMENT-" + UUID.randomUUID().toString().substring(0, 8);
    }
}
