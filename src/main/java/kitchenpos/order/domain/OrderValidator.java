package kitchenpos.order.domain;

import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {
    public void validate(Order order) {
        order.validate();
    }

}
