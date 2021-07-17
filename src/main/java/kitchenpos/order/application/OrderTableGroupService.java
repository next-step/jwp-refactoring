package kitchenpos.order.application;

import java.util.List;

import kitchenpos.order.domain.Order;

public interface OrderTableGroupService {
    List<Order> findOrdersByOrderTableIdIn(List<Long> orderTableIds);
}
