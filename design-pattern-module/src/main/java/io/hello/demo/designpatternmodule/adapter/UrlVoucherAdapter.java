package io.hello.demo.designpatternmodule.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class UrlVoucherAdapter implements VoucherAdapter{

    private final Logger log = LoggerFactory.getLogger(UrlVoucherAdapter.class);

    @Override
    public StandardVoucher convert(SupplierVoucher supplierVoucher) {
        log.info("Appending URL voucher");
        return new StandardVoucher(
                VoucherType.URL,
                supplierVoucher.getVoucherUrl()
        );
    }

    @Override
    public boolean supports(SupplierVoucher supplierVoucher) {
        return supplierVoucher.getContentType().equals(MediaType.TEXT_HTML_VALUE) ||
                supplierVoucher.getContentType().equals(MediaType.TEXT_PLAIN_VALUE) ||
                supplierVoucher.getContentType().equals(MediaType.TEXT_MARKDOWN_VALUE);
    }
}
