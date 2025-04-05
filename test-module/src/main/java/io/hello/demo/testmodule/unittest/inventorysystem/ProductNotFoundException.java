package io.hello.demo.testmodule.unittest.inventorysystem;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {
        super(message);
    }
}
