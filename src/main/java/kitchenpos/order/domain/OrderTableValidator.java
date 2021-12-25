package kitchenpos.order.domain;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderTableValidator {
    public static final List<OrderStatus> PROGRESSING_ORDER_STATUSES = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateHasProgressOrder(OrderTable orderTable) {
        List<Order> orderList = orderRepository.findByOrderTableIdAndOrderStatusIn(orderTable.getId(), PROGRESSING_ORDER_STATUSES);
        if (!orderList.isEmpty()) {
            throw new IllegalArgumentException("주문이 진행되어 테이블 비움 상태를 변경할 수 없습니다");
        }
    }
}
