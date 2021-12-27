package kitchenpos.order.domain;

import java.util.List;
import kitchenpos.ordertable.exception.OrderIsNotCompleteException;
import org.springframework.stereotype.Component;

@Component
public class OrderValidator {

    private final OrderRepository orderRepository;

    public OrderValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateAllOrdersInTableComplete(Long tableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(tableId);
        orders.stream()
            .forEach(order -> validateOrderIsComplete(order));
    }

    private void validateOrderIsComplete(Order order) {
        if (!order.isCompleteStatus()) {
            throw new OrderIsNotCompleteException();
        }
    }
}
