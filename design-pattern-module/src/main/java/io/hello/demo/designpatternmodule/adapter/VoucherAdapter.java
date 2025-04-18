package io.hello.demo.designpatternmodule.adapter;

public interface VoucherAdapter {

    StandardVoucher convert(SupplierVoucher supplierVoucher);
    boolean supports(SupplierVoucher supplierVoucher);

}
