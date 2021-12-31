package kitchenpos.domain.order.validator;

import kitchenpos.domain.order.domain.Order;

public interface OrderCreateValidator {
    void validate(Order order);
}
