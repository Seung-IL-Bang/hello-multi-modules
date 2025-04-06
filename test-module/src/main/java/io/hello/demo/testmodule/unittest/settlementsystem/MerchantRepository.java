package io.hello.demo.testmodule.unittest.settlementsystem;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepository {


    Optional<Merchant> findById(String merchantId);
}
