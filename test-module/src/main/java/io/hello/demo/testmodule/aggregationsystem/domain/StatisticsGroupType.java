package io.hello.demo.testmodule.aggregationsystem.domain;

import io.hello.demo.testmodule.aggregationsystem.storage.Payment;
import io.hello.demo.testmodule.aggregationsystem.storage.PaymentMethod;
import io.hello.demo.testmodule.aggregationsystem.storage.ProductCategory;

public enum StatisticsGroupType {
    PAYMENT_METHOD {
        @Override
        public String getValueFrom(Payment payment) {
            return payment.getPaymentMethod().name();
        }

        @Override
        public void setValueTo(DetailData detail, String value) {
            detail.setPaymentMethod(PaymentMethod.valueOf(value));
        }
    },
    PRODUCT_CATEGORY {
        @Override
        public String getValueFrom(Payment payment) {
            return payment.getProductCategory().name();
        }

        @Override
        public void setValueTo(DetailData detail, String value) {
            detail.setProductCategory(ProductCategory.valueOf(value));
        }
    };

    public abstract String getValueFrom(Payment payment);

    public abstract void setValueTo(DetailData detail, String value);
}
