package kitchenpos.domain;

import kitchenpos.repository.OrderRepository;
import kitchenpos.repository.OrderTableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderTableValidator {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    public OrderTable findExistsOrderTableById(Long id) {
        return orderTableRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
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
