package io.hello.demo.designpatternmodule.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.util.List;

class VoucherDeliveryServiceTest {

    private VoucherDeliveryService voucherDeliveryService;

    @BeforeEach
    void setUp() {


        UrlVoucherAdapter e1 = new UrlVoucherAdapter();
        List<VoucherAdapter> adapters = List.of(e1, new ImageVoucherAdapter(), new PdfVoucherAdapter());
        VoucherAdapterManager voucherAdapterManager
                = new VoucherAdapterManager(adapters);
        VoucherMergeService voucherMergeService = new VoucherMergeService();

        voucherDeliveryService = new VoucherDeliveryService(voucherAdapterManager, voucherMergeService);
    }

    @Test
    void testProcessAndDeliverVoucher() {
        List<SupplierVoucher> supplierVouchers = List.of(
                new SupplierVoucher( "http://example.com/voucher1", MediaType.TEXT_HTML_VALUE),
                new SupplierVoucher("http://example.com/voucher2", MediaType.IMAGE_PNG_VALUE),
                new SupplierVoucher( "http://example.com/voucher3", MediaType.APPLICATION_PDF_VALUE)
        );

        voucherDeliveryService.processAndDeliverVoucher(supplierVouchers);
    }
}