package kitchenpos.orders.order.infra;

import java.util.List;
import kitchenpos.orders.order.domain.MenuRepository;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestMenuClient implements MenuRepository {

    private final RestTemplate restTemplate;

    public RestMenuClient(final RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    @Override
    public boolean existAll(final List<Long> menuIds) {
        return true;
    }
}
