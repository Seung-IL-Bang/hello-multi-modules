package io.hello.demo;

import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;

/**
 * Custom InfoContributor to add custom information to the Actuator info endpoint.
 * https://docs.spring.io/spring-boot/reference/actuator/endpoints.html#actuator.endpoints.info.writing-custom-info-contributors
 */
@Component
public class MyInfoContributor implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("myInfo1", "This is my custom info");
        builder.withDetail("myInfo2", Collections.singletonMap("key2", "value2"));
        builder.withDetails(Map.of(
                "myInfo3", Collections.singletonMap("key3", "value3"),
                "myInfo4", "This is my custom info 4"));
    }
}
