package io.hello.demo.feignmodule;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "external-api", url = "${external.api.url}", configuration = {FeignClientConfig.class})
public interface ExternalApiClient {

    @GetMapping("/external/api/v1")
    ResponseEntity<?> getDelayApi(@RequestParam(name = "delay", required = false, defaultValue = "0") long delay);

    @GetMapping("/external/api/v1/error")
    ResponseEntity<?> getDelayApiWithError(@RequestParam(name = "delay", required = false, defaultValue = "0") long delay);

    @GetMapping("/external/api/v1/exception")
    ResponseEntity<?> getDelayApiWithException(@RequestParam(name = "delay", required = false, defaultValue = "0") long delay);

    @GetMapping("/external/api/v1/exception/status")
    ResponseEntity<?> getDelayApiWithException(@RequestParam(name = "delay", required = false, defaultValue = "0") long delay,
                                                @RequestParam(name = "code", required = false, defaultValue = "500") int code);
}
