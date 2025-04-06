package io.hello.demo.testmodule.unittest.simplesystem.paymentstatus;

import org.springframework.stereotype.Service;

@Service
public interface RefundService {
    void processRefund(Long paymentId, Long amount);
}
