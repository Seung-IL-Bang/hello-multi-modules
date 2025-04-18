package io.hello.demo.designpatternmodule.adapter;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VoucherAdapterManager {

    private final List<VoucherAdapter> adapters;

    public VoucherAdapterManager(List<VoucherAdapter> adapters) {
        this.adapters = adapters;
    }

    public StandardVoucher convert(SupplierVoucher supplierVoucher) {
        for (VoucherAdapter adapter : adapters) {
            if (adapter.supports(supplierVoucher)) {
                return adapter.convert(supplierVoucher);
            }
        }
        throw new IllegalArgumentException("No suitable adapter found for the given supplier voucher.");
    }
}
