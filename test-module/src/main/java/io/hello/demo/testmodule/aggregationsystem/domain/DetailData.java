package io.hello.demo.testmodule.aggregationsystem.domain;

import io.hello.demo.testmodule.aggregationsystem.storage.PaymentMethod;
import io.hello.demo.testmodule.aggregationsystem.storage.ProductCategory;

import java.util.Map;

public class DetailData {
    private PaymentMethod paymentMethod;
    private ProductCategory productCategory;
    private Number amount;
    private Integer count;
    private Map<String, Object> additionalData;

    // Getters and Setters
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public ProductCategory getProductCategory() { return productCategory; }
    public void setProductCategory(ProductCategory productCategory) { this.productCategory = productCategory; }
    public Number getAmount() { return amount; }
    public void setAmount(Number amount) { this.amount = amount; }
    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
    public Map<String, Object> getAdditionalData() { return additionalData; }
    public void setAdditionalData(Map<String, Object> additionalData) { this.additionalData = additionalData; }
}
