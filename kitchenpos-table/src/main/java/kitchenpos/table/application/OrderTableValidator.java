package kitchenpos.table.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class OrderTableValidator {
    private static final String INVALID_CHANGE_ORDER_STATUS = "변경 할 수 없는 주문 상태입니다";

    private final OrderRepository orderRepository;

    public OrderTableValidator(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void validateEmptyStatus(Long orderTableId) {
        if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
                orderTableId, Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new IllegalStateException(INVALID_CHANGE_ORDER_STATUS);
        }
    }
}
