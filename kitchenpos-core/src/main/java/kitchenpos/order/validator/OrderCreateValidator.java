package kitchenpos.order.validator;

import kitchenpos.order.domain.Order;

public interface OrderCreateValidator {
    void validate(Order order);
}
