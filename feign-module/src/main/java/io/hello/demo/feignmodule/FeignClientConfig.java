package io.hello.demo.feignmodule;

import feign.*;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Configuration
public class FeignClientConfig {

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return new DefaultFeignErrorDecoder();
    }

//    @Bean
//    public Request.Options options() {
//        return new Request.Options(5, TimeUnit.SECONDS, 5, TimeUnit.SECONDS, true);
//    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000L, TimeUnit.SECONDS.toMillis(5L), 5);
    }

    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return feign.Logger.Level.FULL;
    }

    public static class DefaultFeignErrorDecoder extends ErrorDecoder.Default {

        private final Logger log = LoggerFactory.getLogger(FeignClientConfig.class);

        @Override
        public Exception decode(String methodKey, Response response) {
            Exception exception = super.decode(methodKey, response); // check response header 'Retry-After'

            log.error("[FeignErrorDecoder] Error occurred: {} - {}", methodKey, response.request().url(), exception);

            return switch (response.status()) {
                case 429, // Too Many Requests
                     503 // Service Unavailable
                        -> new RetryableException(
                        response.status(),
                        response.reason(),
                        response.request().httpMethod(),
                        new Date().getTime(), // In fact, It's Ignored if you set the retryer
                        response.request());
                default -> exception;
            };

        }
    }
}
