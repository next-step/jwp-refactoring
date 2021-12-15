package kitchenpos.domain.table.application;

import kitchenpos.domain.order.domain.Order;
import kitchenpos.domain.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderTableValidator {

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateCompleteTable(Long orderTableId) {
        final Optional<Order> savedOrder = orderRepository.findByOrderTableId(orderTableId);
        if (!savedOrder.isPresent()) {
            throw new IllegalArgumentException();
        }
        final Order order = savedOrder.get();
        if (!order.isComplete()) {
            throw new IllegalArgumentException();
        }
    }
}
