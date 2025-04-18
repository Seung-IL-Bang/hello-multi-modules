package io.hello.demo.designpatternmodule.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherMergeService {

    private final Logger log = LoggerFactory.getLogger(VoucherMergeService.class);

    public MergedVoucher merge(List<StandardVoucher> vouchers) {
        for (StandardVoucher voucher : vouchers) {
            switch (voucher.getType()) {
                case PDF:
                    log.info("Merging PDF voucher");
                    break;
                case IMAGE:
                    log.info("Merging image voucher");
                    break;
                case URL:
                    log.info("Merging URL voucher");
                    break;
                default:
                    throw new IllegalArgumentException("Unknown voucher type: " + voucher.getType());
            }
        }
        return new MergedVoucher("Merged voucher content");
    }


}
