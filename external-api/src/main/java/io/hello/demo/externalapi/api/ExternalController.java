package io.hello.demo.externalapi.api;

import io.hello.demo.externalapi.support.error.ErrorType;
import io.hello.demo.externalapi.support.response.ApiResponse;
import io.hello.demo.utilsmodule.ThreadUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/external")
public class ExternalController {

    /**
     * Simulate an external API call with a delay
     * @param delay
     * @return
     */
    @GetMapping("/api/v1")
    public ApiResponse<?> getDelayApi(@RequestParam(
            name = "delay",
            required = false,
            defaultValue = "0") long delay) {
        ThreadUtils.sleep(delay); // Simulate a delay of 1 second
        return ApiResponse.success();
    }

    /**
     * Simulate an external API call with a delay and an error
     * @param delay
     * @return
     */
    @GetMapping("/api/v1/error")
    public ApiResponse<?> getDelayApiWithThrowable(@RequestParam(
            name = "delay",
            required = false,
            defaultValue = "0") long delay) {
        ThreadUtils.sleep(delay); // Simulate a delay of 1 second
        return ApiResponse.error(ErrorType.DEFAULT_ERROR);
    }
}
