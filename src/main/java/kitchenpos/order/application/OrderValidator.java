package kitchenpos.order.application;

import kitchenpos.order.domain.Order;

import java.util.List;

public interface OrderValidator {

    void validateOrder(Order order);
    void validateUngroup(List<Long> orderTableIds);
}
