package kitchenpos.tobe.menus.menu.infra;

import java.util.List;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestProductClient {

    private final RestTemplate restTemplate;

    public RestProductClient(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public boolean existAll(final List<Long> productIds) {
        return true;
    }
}
