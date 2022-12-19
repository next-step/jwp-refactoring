package kitchenpos.order.validator;

import kitchenpos.order.domain.Order;

public interface OrderValidators {

    void validateCreation(Order order);
}
