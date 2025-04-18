package io.hello.demo.designpatternmodule.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherDeliveryService {

    private final Logger log = LoggerFactory.getLogger(VoucherDeliveryService.class);

    private final VoucherAdapterManager voucherAdapterManager;
    private final VoucherMergeService voucherMergeService;

    public VoucherDeliveryService(VoucherAdapterManager voucherAdapterManager, VoucherMergeService voucherMergeService) {
        this.voucherAdapterManager = voucherAdapterManager;
        this.voucherMergeService = voucherMergeService;
    }

    public void processAndDeliverVoucher(List<SupplierVoucher> supplierVouchers) {
        List<StandardVoucher> standardVouchers = supplierVouchers.stream()
                .map(voucherAdapterManager::convert)
                .toList();

        MergedVoucher mergedVoucher = voucherMergeService.merge(standardVouchers);

        log.info("Delivering merged voucher: {}", mergedVoucher.content());
    }
}
