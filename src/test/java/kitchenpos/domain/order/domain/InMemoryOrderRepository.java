package kitchenpos.domain.order.domain;

import kitchenpos.common.InMemoryRepository;

import java.util.List;
import java.util.Optional;

public class InMemoryOrderRepository extends InMemoryRepository<Order> implements OrderRepository {

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses) {
        return db.values().stream()
                .filter(order -> orderTableIds.contains(order.getOrderTableId()))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public Optional<Order> findByOrderTableId(Long orderTableId) {
        return db.values().stream()
                .filter(order -> order.getOrderTableId() == orderTableId)
                .findFirst();
    }
}
