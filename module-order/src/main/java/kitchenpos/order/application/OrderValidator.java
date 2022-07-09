package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderTableValidationEvent;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator extends AbstractAggregateRoot<OrderValidator> {
    public void validateOrder(final Order order) {
        this.registerEvent(new OrderTableValidationEvent(order));
    }
}
