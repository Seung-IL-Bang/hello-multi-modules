package io.hello.demo.testmodule.unittest.inventorysystem;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository {

    Optional<Product> findById(String productId);

    Product save(Product product);

    List<Product> findAll();
}
