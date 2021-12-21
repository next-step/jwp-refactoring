package kichenpos.order.config;

import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public Retryer retryer() {
        return new Retryer.Default();
    }

    @Bean
    public ErrorDecoder decoder() {
        return (methodKey, response) -> {
            if (HttpStatus.valueOf(response.status()).is5xxServerError()) {
                return new RetryableException(
                    response.status(),
                    String.format("%s 요청에 서버 오류가 있어서 재시도 status: %d",
                        methodKey, response.status()),
                    response.request().httpMethod(),
                    null,
                    response.request());
            }

            return new IllegalStateException(String
                .format("%s 요청 실패 status: %d", methodKey, response.status()));
        };
    }
}


