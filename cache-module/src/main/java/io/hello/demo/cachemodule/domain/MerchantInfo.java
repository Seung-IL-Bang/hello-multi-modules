package io.hello.demo.cachemodule.domain;

public record MerchantInfo(
        String id,
        String name,
        String businessNumber,
        double feeRate
) {
}
