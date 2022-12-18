package kitchenpos.table.validator;

import kitchenpos.order.domain.Order;
import kitchenpos.order.persistence.OrderRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TableValidator {
    private final OrderRepository orderRepository;

    public TableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateTableEmpty(Long orderTableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(orderTableId);
        orders.forEach(Order::validateBeforeCompleteStatus);
    }
}

