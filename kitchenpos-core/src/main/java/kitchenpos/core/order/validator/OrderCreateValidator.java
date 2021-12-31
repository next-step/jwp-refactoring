package kitchenpos.core.order.validator;

import kitchenpos.core.order.domain.Order;

public interface OrderCreateValidator {
    void validate(Order order);
}
