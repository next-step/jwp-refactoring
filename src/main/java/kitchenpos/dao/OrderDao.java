package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

import java.util.List;
import java.util.Optional;

public interface OrderDao {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);

    Optional<Order> findByOrderTableId(Long orderTableId);
}
