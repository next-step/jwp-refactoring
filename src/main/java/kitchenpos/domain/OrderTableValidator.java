package kitchenpos.domain;

import kitchenpos.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTableValidator {
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void checkOrderStatus(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        if (!isAllCompletion(orders)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isAllCompletion(List<Order> orders) {
        return orders.stream()
                .allMatch(this::isCompletion);
    }

    private boolean isCompletion(Order order) {
        return order.isCompletion();
    }
}
