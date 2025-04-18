package io.hello.demo.designpatternmodule.adapter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class ImageVoucherAdapter implements VoucherAdapter {

    private final Logger log = LoggerFactory.getLogger(ImageVoucherAdapter.class);

    @Override
    public StandardVoucher convert(SupplierVoucher supplierVoucher) {
        log.info("Appending image voucher");
        return new StandardVoucher(VoucherType.IMAGE, supplierVoucher.getVoucherUrl());
    }

    @Override
    public boolean supports(SupplierVoucher supplierVoucher) {
        return supplierVoucher.getContentType().equals(MediaType.IMAGE_PNG_VALUE) ||
                supplierVoucher.getContentType().equals(MediaType.IMAGE_GIF_VALUE) ||
                supplierVoucher.getContentType().equals(MediaType.IMAGE_JPEG_VALUE);
    }
}
