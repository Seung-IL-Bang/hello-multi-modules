package io.hello.demo.webmodule.threads;

import io.hello.demo.utilsmodule.ThreadUtils;
import io.hello.demo.webmodule.support.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web-module")
public class TomcatThreadsController {

    @GetMapping("/tomcat-threads")
    public ApiResponse<?> tomcatThreads() {

        ThreadUtils.sleep(2000);

        return ApiResponse.success();
    }
}
