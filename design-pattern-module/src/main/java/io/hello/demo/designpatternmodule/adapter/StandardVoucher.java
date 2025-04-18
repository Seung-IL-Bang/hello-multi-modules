package io.hello.demo.designpatternmodule.adapter;

public class StandardVoucher {

    private VoucherType type;
    private String voucherUrl;

    public StandardVoucher(VoucherType type, String voucherUrl) {
        this.type = type;
        this.voucherUrl = voucherUrl;
    }

    public VoucherType getType() {
        return type;
    }

    public String getVoucherUrl() {
        return voucherUrl;
    }
}
