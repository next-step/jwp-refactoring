package kitchenpos.order.validator;

import kitchenpos.order.domain.Order;
import org.springframework.stereotype.Component;

@Component
public interface OrderValidator {

    void validateCreate(Order order);
}
