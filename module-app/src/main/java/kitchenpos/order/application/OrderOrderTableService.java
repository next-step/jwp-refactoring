package kitchenpos.order.application;

import java.util.Optional;

import kitchenpos.order.domain.Order;

public interface OrderOrderTableService {
    Optional<Order> findOrderByOrderTableId(Long orderTableId);
}
