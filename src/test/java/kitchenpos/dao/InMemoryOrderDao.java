package kitchenpos.dao;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;

import java.util.List;
import java.util.Optional;

public class InMemoryOrderDao extends InMemoryDao<Order> implements OrderDao {

    @Override
    public boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses) {
        return db.values().stream()
                .filter(order -> order.getOrderTable().getId() == orderTableId)
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses) {
        return db.values().stream()
                .filter(order -> orderTableIds.contains(order.getOrderTable().getId()))
                .anyMatch(order -> orderStatuses.contains(order.getOrderStatus()));
    }

    @Override
    public Optional<Order> findByOrderTableId(Long orderTableId) {
        return db.values().stream()
                .filter(order -> order.getOrderTable().getId() == orderTableId)
                .findFirst();
    }
}
