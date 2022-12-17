package kitchenpos.validator.order;

import kitchenpos.order.domain.Order;

public interface OrderValidator {

    void validate(Order order);
}
