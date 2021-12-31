package kitchenpos.order.domain.validator;

import kitchenpos.order.domain.Order;

public interface OrderCreateValidator {
    void validate(Order order);
}
