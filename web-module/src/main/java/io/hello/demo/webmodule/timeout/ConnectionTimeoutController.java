package io.hello.demo.webmodule.timeout;

import io.hello.demo.webmodule.support.error.ErrorCode;
import io.hello.demo.webmodule.support.error.ErrorMessage;
import io.hello.demo.webmodule.support.error.ErrorType;
import io.hello.demo.webmodule.support.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import static io.hello.demo.webmodule.support.error.ErrorType.NETWORK_ERROR;
import static io.hello.demo.webmodule.support.error.ErrorType.UNKNOWN_ERROR;

@RestController
@RequestMapping("/web-module")
public class ConnectionTimeoutController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * Connection Timeout Test
     *
     * @param delay
     * @return
     */
    @GetMapping("/connection-timeout")
    public ApiResponse<?> connectionTimeout() {

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(2000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);

        // 더 확실한 connection timeout을 유발하는 IP와 포트 사용
        String nonRoutableIp = "192.0.2.1"; // TEST-NET-1 블록 (RFC 5737)
        String url = "http://" + nonRoutableIp + ":81/api";  // 일반적으로 사용되지 않는 포트

        try {
            ResponseEntity<ApiResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    ApiResponse.class
            );
            return response.getBody();
        } catch (ResourceAccessException e) {
            log.error("[Error][ConnectionTimeoutController][connectionTimeout] : {}", e.getMessage(), e);
            if (e.getCause() instanceof ConnectException) { // java.net.ConnectException 이 ResourceAccessException 에 포함되어 발생
                return ApiResponse.error(ErrorType.CONNECTION_TIMEOUT);
            }
            if (e.getCause() instanceof SocketTimeoutException ste) { // connectionTimeout 또는 readTimeout 발생
                return ApiResponse.error(new ErrorMessage(ErrorCode.E500.name(), ste.getMessage()));
            }
            return ApiResponse.error(NETWORK_ERROR);
        } catch (Exception e) {
            log.error("[Error][ConnectionTimeoutController][connectionTimeout] : {}", e.getMessage(), e);
            return ApiResponse.error(UNKNOWN_ERROR);
        }
    }
}
