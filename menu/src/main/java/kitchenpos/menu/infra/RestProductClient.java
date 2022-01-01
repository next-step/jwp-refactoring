package kitchenpos.menu.infra;

import java.util.List;
import kitchenpos.menu.domain.ProductRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestProductClient implements ProductRepository {

    private final RestTemplate restTemplate;

    public RestProductClient(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public boolean existAll(final List<Long> productIds) {
        return true;
    }
}
