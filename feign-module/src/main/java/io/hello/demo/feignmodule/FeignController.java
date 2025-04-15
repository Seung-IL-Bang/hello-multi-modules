package io.hello.demo.feignmodule;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/feign")
public class FeignController {

    private final ExternalApiClient externalApiClient;

    public FeignController(ExternalApiClient externalApiClient) {
        this.externalApiClient = externalApiClient;
    }

    @GetMapping("/api/v1")
    public ResponseEntity<?> getDelayApi(@RequestParam(value = "delay", required = false, defaultValue = "0") long delay) {
        return externalApiClient.getDelayApi(delay);
    }

    @GetMapping("/api/v1/error")
    public ResponseEntity<?> getDelayApiWithErrorResponse(@RequestParam(value = "delay", required = false, defaultValue = "0") long delay) {
        return externalApiClient.getDelayApiWithError(delay);
    }

    @GetMapping("/api/v1/exception")
    public ResponseEntity<?> getDelayApiWithException(@RequestParam(value = "delay", required = false, defaultValue = "0") long delay) {
        return externalApiClient.getDelayApiWithException(delay);
    }

    @GetMapping("/api/v1/exception/status")
    public ResponseEntity<?> getDelayApiWithException(@RequestParam(value = "delay", required = false, defaultValue = "0") long delay,
                                                      @RequestParam(value = "code", required = false, defaultValue = "500") int code) {
        return externalApiClient.getDelayApiWithException(delay, code);
    }
}
