package io.hello.demo.webmodule;

import io.hello.demo.webmodule.support.error.ErrorCode;
import io.hello.demo.webmodule.support.error.ErrorMessage;
import io.hello.demo.webmodule.support.response.ApiResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.SocketTimeoutException;

import static io.hello.demo.webmodule.support.error.ErrorType.NETWORK_ERROR;
import static io.hello.demo.webmodule.support.error.ErrorType.UNKNOWN_ERROR;

@RestController
@RequestMapping("/web-module")
public class ReadTimeoutController {

    /**
     * RestTemplate 은 Spring Framework 5.0 이상부터 Deprecated 되었으며, WebClient 를 사용하도록 권장됨
     */

    // external-api 와 통신하여 readTimeout 을 테스트하는 메소드
    @GetMapping("/read-timeout")
    public ApiResponse<?> readTimeout(@RequestParam(value = "delay", required = false, defaultValue = "0") long delay) {

        RestTemplate restTemplate = new RestTemplate();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setReadTimeout(5000);
        restTemplate.setRequestFactory(requestFactory);

        String url = "http://localhost:4444/external/api/v1"; // external-api 모듈의 url

        if (delay > 0) {
            url += "?delay=" + delay;
        }

        try {
            ResponseEntity<ApiResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    ApiResponse.class
            );
            return response.getBody();
        } catch (ResourceAccessException e) {
            if (e.getCause() instanceof SocketTimeoutException ste) { // readTimeout 발생
                return ApiResponse.error(new ErrorMessage(ErrorCode.E500.name(), ste.getMessage()));
            }
            return ApiResponse.error(NETWORK_ERROR);
        } catch (Exception e) {
            return ApiResponse.error(UNKNOWN_ERROR);
        }
    }
}
