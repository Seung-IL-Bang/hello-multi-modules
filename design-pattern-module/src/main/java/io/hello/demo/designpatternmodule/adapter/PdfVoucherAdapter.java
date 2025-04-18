package io.hello.demo.designpatternmodule.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class PdfVoucherAdapter implements VoucherAdapter{

    private final Logger log = LoggerFactory.getLogger(PdfVoucherAdapter.class);

    @Override
    public StandardVoucher convert(SupplierVoucher supplierVoucher) {
        log.info("Appending PDF voucher");
        return new StandardVoucher(
                VoucherType.PDF,
                supplierVoucher.getVoucherUrl()
        );
    }

    @Override
    public boolean supports(SupplierVoucher supplierVoucher) {
        return supplierVoucher.getContentType().equals(MediaType.APPLICATION_PDF_VALUE);
    }
}
