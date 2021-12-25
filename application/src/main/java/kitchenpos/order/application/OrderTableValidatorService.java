package kitchenpos.order.application;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableValidator;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class OrderTableValidatorService implements OrderTableValidator {
    public static final List<OrderStatus> PROGRESSING_ORDER_STATUSES = Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL);
    private final OrderRepository orderRepository;

    public OrderTableValidatorService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateHasProgressOrder(OrderTable orderTable) {
        List<Order> orderList = orderRepository.findByOrderTableIdAndOrderStatusIn(orderTable.getId(), PROGRESSING_ORDER_STATUSES);
        if (!orderList.isEmpty()) {
            throw new IllegalArgumentException("주문이 진행되어 테이블 비움 상태를 변경할 수 없습니다");
        }
    }
}
