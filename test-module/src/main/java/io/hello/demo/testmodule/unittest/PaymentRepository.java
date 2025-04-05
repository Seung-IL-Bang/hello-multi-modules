package io.hello.demo.testmodule.unittest;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository {
    Payment save(Payment payment);
    Optional<Payment> findById(String paymentId);
}
