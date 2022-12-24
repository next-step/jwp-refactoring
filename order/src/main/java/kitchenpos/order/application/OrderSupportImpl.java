package kitchenpos.order.application;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.OrderSupport;

@Component
public class OrderSupportImpl implements OrderSupport {
    public static final List<OrderStatus> ORDER_STATUSES_UNCHANGEABLE = Arrays.asList(OrderStatus.COOKING,
        OrderStatus.MEAL);
    private final OrderRepository orderRepository;

    public OrderSupportImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean validateOrderChangeable(Long orderTableId) {
        return orderRepository.existsByOrderTableIdAndOrderStatusIn(orderTableId, ORDER_STATUSES_UNCHANGEABLE);
    }

    @Override
    public boolean validateOrderChangeable(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds, ORDER_STATUSES_UNCHANGEABLE);
    }
}
