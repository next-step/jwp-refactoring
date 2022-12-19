package kitchenpos.order.domain;

import org.springframework.stereotype.Component;

@Component
public interface OrderValidator {
    void validate(Order order);
}
