package kitchenpos.order.service;

import kitchenpos.domain.Order;
import kitchenpos.repository.OrderRepository;
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
    public boolean isNotCompleteOrder(long tableId) {
        return !Optional.ofNullable(orderRepository.findByOrderTableIdEquals(tableId))
                .map(Order::isComplete)
                .orElse(true);
    }

}
