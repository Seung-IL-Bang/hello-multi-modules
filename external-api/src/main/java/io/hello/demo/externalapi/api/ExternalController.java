package io.hello.demo.externalapi.api;

import io.hello.demo.externalapi.support.error.CoreException;
import io.hello.demo.externalapi.support.error.ErrorType;
import io.hello.demo.externalapi.support.response.ApiResponse;
import io.hello.demo.utilsmodule.ThreadUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/external")
public class ExternalController {

    /**
     * Simulate an external API call with a delay
     *
     * @param delay
     * @return
     */
    @GetMapping("/api/v1")
    public ApiResponse<?> getDelayApi(@RequestParam(
            name = "delay",
            required = false,
            defaultValue = "0") long delay) {
        ThreadUtils.sleep(delay); // Simulate a delay
        return ApiResponse.success();
    }

    /**
     * Simulate an external API call with a delay and an error response
     *
     * @param delay
     * @return
     */
    @GetMapping("/api/v1/error")
    public ApiResponse<?> getDelayApiWithErrorResponse(@RequestParam(
            name = "delay",
            required = false,
            defaultValue = "0") long delay) {
        ThreadUtils.sleep(delay); // Simulate a delay
        return ApiResponse.error(ErrorType.DEFAULT_ERROR);
    }

    /**
     * Simulate an external API call with a delay and Throw an exception
     *
     * @param delay
     * @return
     */
    @GetMapping("/api/v1/exception")
    public ApiResponse<?> getDelayApiWithException(@RequestParam(
            name = "delay",
            required = false,
            defaultValue = "0") long delay) {
        ThreadUtils.sleep(delay); // Simulate a delay
        throw new CoreException(ErrorType.DEFAULT_ERROR);
    }

    @GetMapping("/api/v1/exception/status")
    public ApiResponse<?> getDelayApiWithExceptionStatus(
            @RequestParam(
                    name = "delay",
                    required = false,
                    defaultValue = "0") long delay,
            @RequestParam(
                    name = "code",
                    required = false,
                    defaultValue = "500") int code) {
        ThreadUtils.sleep(delay); // Simulate a delay
        throw new ResponseStatusException(HttpStatusCode.valueOf(code));
    }
}
