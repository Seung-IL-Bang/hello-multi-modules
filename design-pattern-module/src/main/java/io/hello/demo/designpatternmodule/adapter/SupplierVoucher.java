package io.hello.demo.designpatternmodule.adapter;

public class SupplierVoucher {

    private String voucherUrl;
    private String contentType;

    public SupplierVoucher(String voucherUrl, String contentType) {
        this.voucherUrl = voucherUrl;
        this.contentType = contentType;
    }

    public String getVoucherUrl() {
        return voucherUrl;
    }

    public String getContentType() {
        return contentType;
    }
}
