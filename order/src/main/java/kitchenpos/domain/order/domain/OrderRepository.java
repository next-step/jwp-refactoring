package kitchenpos.domain.order.domain;

import kitchenpos.domain.order.domain.Order;
import kitchenpos.domain.order.domain.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

    Optional<Order> findByOrderTableId(Long orderTableId);
}
