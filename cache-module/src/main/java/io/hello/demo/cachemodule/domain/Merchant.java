package io.hello.demo.cachemodule.domain;

public class Merchant {

    private String id;
    private String name;
    private String businessNumber;

    public Merchant(String id, String name, String businessNumber) {
        this.id = id;
        this.name = name;
        this.businessNumber = businessNumber;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBusinessNumber() {
        return businessNumber;
    }

}
