package io.hello.demo.paymentapi.api.controller.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.hello.demo.paymentapi.api.controller.request.info.CreditCardPaymentRequestInfo;
import io.hello.demo.paymentapi.api.controller.request.info.MobilePaymentRequestInfo;
import io.hello.demo.paymentapi.api.controller.request.info.VirtualAccountPaymentRequestInfo;
import io.hello.demo.paymentapi.domain.method.PaymentMethodType;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaymentRequestDto {

    private String orderId;
    private String productId;
    private String userId;
    private BigDecimal amount;
    private int quantity;

    private PaymentMethodType paymentMethodType;

    private CreditCardPaymentRequestInfo creditCardInfo;
    private VirtualAccountPaymentRequestInfo virtualAccountInfo;
    private MobilePaymentRequestInfo mobileInfo;

    public PaymentRequestDto() {
    }

    public PaymentRequestDto(String orderId,
                             String userId,
                             BigDecimal amount,
                             PaymentMethodType paymentMethodType,
                             CreditCardPaymentRequestInfo creditCardInfo,
                             VirtualAccountPaymentRequestInfo virtualAccountInfo,
                             MobilePaymentRequestInfo mobileInfo) {
        this.orderId = orderId;
        this.userId = userId;
        this.amount = amount;
        this.paymentMethodType = paymentMethodType;
        this.creditCardInfo = creditCardInfo;
        this.virtualAccountInfo = virtualAccountInfo;
        this.mobileInfo = mobileInfo;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethodType getPaymentMethodType() {
        return paymentMethodType;
    }

    public void setPaymentMethodType(PaymentMethodType paymentMethodType) {
        this.paymentMethodType = paymentMethodType;
    }

    public CreditCardPaymentRequestInfo getCreditCardInfo() {
        return creditCardInfo;
    }

    public void setCreditCardInfo(CreditCardPaymentRequestInfo creditCardInfo) {
        this.creditCardInfo = creditCardInfo;
    }

    public VirtualAccountPaymentRequestInfo getVirtualAccountInfo() {
        return virtualAccountInfo;
    }

    public void setVirtualAccountInfo(VirtualAccountPaymentRequestInfo virtualAccountInfo) {
        this.virtualAccountInfo = virtualAccountInfo;
    }

    public MobilePaymentRequestInfo getMobileInfo() {
        return mobileInfo;
    }

    public void setMobileInfo(MobilePaymentRequestInfo mobileInfo) {
        this.mobileInfo = mobileInfo;
    }

    public static class Builder {
        private String orderId;
        private String productId;
        private int quantity;
        private String userId;
        private BigDecimal amount;
        private PaymentMethodType paymentMethodType;
        private CreditCardPaymentRequestInfo creditCardInfo;
        private VirtualAccountPaymentRequestInfo virtualAccountInfo;
        private MobilePaymentRequestInfo mobileInfo;

        public Builder orderId(String orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder productId(String productId) {
            this.productId = productId;
            return this;
        }

        public Builder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder amount(BigDecimal amount) {
            this.amount = amount;
            return this;
        }

        public Builder paymentMethodType(PaymentMethodType paymentMethodType) {
            this.paymentMethodType = paymentMethodType;
            return this;
        }

        public Builder creditCardInfo(CreditCardPaymentRequestInfo creditCardInfo) {
            this.creditCardInfo = creditCardInfo;
            return this;
        }

        public Builder virtualAccountInfo(VirtualAccountPaymentRequestInfo virtualAccountInfo) {
            this.virtualAccountInfo = virtualAccountInfo;
            return this;
        }

        public Builder mobileInfo(MobilePaymentRequestInfo mobileInfo) {
            this.mobileInfo = mobileInfo;
            return this;
        }

        public PaymentRequestDto build() {
            return new PaymentRequestDto(orderId, userId, amount, paymentMethodType, creditCardInfo, virtualAccountInfo, mobileInfo);
        }
    }
}
