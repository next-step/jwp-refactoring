package kitchenpos.ordertable.domain;

import java.util.List;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderTableValidator {
    private static final String EXIST_NOT_COMPLETION_ORDER = "완료되지 않은 주문이 존재합니다.";
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateOrderStatus(Long tableId) {
        List<Order> orders = orderRepository.findAllByOrderTableId(tableId);
        orders.stream()
                .filter(order -> !order.isComplete())
                .findFirst()
                .ifPresent(order -> {
                    throw new IllegalArgumentException(EXIST_NOT_COMPLETION_ORDER);
                });
    }
}
