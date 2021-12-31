package kitchenpos.core.validator;

import kitchenpos.core.domain.Order;

public interface OrderCreateValidator {
    void validate(Order order);
}
