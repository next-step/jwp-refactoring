package kitchenpos.order.domain;

import java.util.Collection;

import org.springframework.stereotype.Component;

import kitchenpos.table.domain.OrderStatusChecker;

@Component
public class OrderStatusCheckerImpl implements OrderStatusChecker {
    private final OrderRepository orderRepository;

    public OrderStatusCheckerImpl(final OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean existsNotCompletedOrderByOrderTableIds(final Collection<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, OrderStatus.getNotCompletions());
    }
}
