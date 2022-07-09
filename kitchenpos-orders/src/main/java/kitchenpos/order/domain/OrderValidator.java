package kitchenpos.order.domain;

import kitchenpos.order.domain.Order;

public interface OrderValidator {
    void validate(Order order);
}
