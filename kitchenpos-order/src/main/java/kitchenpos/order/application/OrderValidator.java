package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public interface OrderValidator {

    void validate(Order order);
}
