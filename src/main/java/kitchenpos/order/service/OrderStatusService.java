package kitchenpos.order.service;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderStatusService {
    private final OrderRepository orderRepository;

    public OrderStatusService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional(readOnly = true)
    public boolean isNotCompleteOrder(OrderTable table) {
        return !Optional.ofNullable(orderRepository.findByOrderTableIdEquals(table))
                .map(Order::isComplete)
                .orElse(true);
    }

}
